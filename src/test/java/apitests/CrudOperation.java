package apitests;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CrudOperation {
	final static String ROOT_URI = "http://localhost:8080/rest/employee/";

	
@Test(dataProvider="getEmployeeUsingId")
public void getEmployeeDetail(String id) {
	get(ROOT_URI+id).then().statusCode(200).extract().response();
}

@Test
public void getEmployeeDetailAndVerify() {
	Response response = get(ROOT_URI);
	System.out.println("Response is: "+response.asString());
	response.then().body("id", hasItems(2));
	response.then().body("firstname", hasItems("Siva"));
}

@Test(dataProvider = "employeeParam")
public void verifyEmployeeDetail(int id, String firstName) {	
	get(ROOT_URI+id).then().body("firstname", Matchers.is(firstName));
}


@Test(dataProvider = "addEmployee",priority = 0)
public void addEmployee(String  id, String firstName, String lastName, String designation, String salary) {	   
	   JsonObject addEmployee = new JsonObject();
	   addEmployee.addProperty("id", id);
	   addEmployee.addProperty("firstname", firstName);
	   addEmployee.addProperty("lastname", lastName);
	   addEmployee.addProperty("designation", designation);
	   addEmployee.addProperty("salary", salary);
	   addEmployee.toString();  	   
	            given()
	            .contentType(ContentType.JSON)
	            .body(addEmployee)
	            .post(ROOT_URI)
	            .then()
	            .statusCode(201)
	            .extract()
	            .response();  	  
}

@Test(dataProvider = "deleteEmployeeUsingId",priority = 1,enabled=true)
public void deleteEmployee(int id) {
	 given()
    .contentType(ContentType.JSON)    
    .delete(ROOT_URI+(id+1))
    .then()
    .statusCode(410)
    .extract()
    .response();	
}

@Test(dataProvider = "updateEmployee")
public void updateEmployee(String id, String firstName, String lastName, String designation, String salary) {
	JsonObject addEmployee = new JsonObject();
	   addEmployee.addProperty("id", id);
	   addEmployee.addProperty("firstname", firstName);
	   addEmployee.addProperty("lastname", lastName);
	   addEmployee.addProperty("designation", designation);
	   addEmployee.addProperty("salary", salary);
	   addEmployee.toString();     
	   
	            given()
	            .contentType(ContentType.JSON)
	            .body(addEmployee)
	            .put(ROOT_URI)
	            .then()
	            .statusCode(200)
	            .extract()
	            .response();	  
}

@Test
public void getAllEmployees() {
	 given()
    .contentType(ContentType.JSON)
    .get(ROOT_URI)
    .then()
    .statusCode(200)
    .extract()
    .response();	
}

@DataProvider
public Object[][] employeeParam() {
	Object[][] testData = new Object[][] { 
		new Object[] { 2, "Siva" }
	};
	return testData;
}

@DataProvider
public Object[][] addEmployee() {
	Object[][] testData = new Object[][] { 
		new Object[] { "1001", "Steve","Weslin","Senior Tester","12000"}
	};
	return testData;
}

@DataProvider
public Object[][] updateEmployee() {
	Object[][] testData = new Object[][] { 
		new Object[] { "3", "Steve","Walker","Senior Tester","19000"}
	};
	return testData;
}

@DataProvider
public Object[][] getEmployeeUsingId() {
	Object[][] testData = new Object[][] { 
		new Object[] { "2"}
	};
	return testData;
}

@DataProvider
public Object[][] deleteEmployeeUsingId() {
	Object[][] testData = new Object[][] { 
		new Object[] {53}
	};
	return testData;
}


}
