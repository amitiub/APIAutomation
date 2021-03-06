import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Validate if Add Place API is working as expected
		// Add place-> Update Place with new address -> Get Place to validate if new address is present in response.
		
		// Given - all input details
		// When - Submit the API
		// Then - Validate the response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String response = given()
							.log().all()
							.baseUri("https://rahulshettyacademy.com")
							.queryParam("key", "qaclick123")
							.header("Content-Type", "application/json")
							.body(payload.AddPlace())
						 .when()
						 	.log().all()
						 	.post("maps/api/place/add/json")
						 .then()
						 	.log().all()
						 	.assertThat()
						 	.statusCode(200)
						 	.body("scope", equalTo("APP"))
						 	.header("server", "Apache/2.4.18 (Ubuntu)")
						 	.extract().response().asString();
		
		JsonPath js = new JsonPath(response); // For parsing JSON
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		// Update address
		
		String newAddress = "6967 Bayers Road, Apt 501, Halifax, NS, B3L 4P2, Canada";
		
		given()
			.log().all()
			.baseUri("https://rahulshettyacademy.com")
			.queryParam("key", "qaclick123")
			.header("Content-Type", "application/json")
			.body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}")
		.when()
			.log().all()
			.put("maps/api/place/update/json")
		.then()
			.log().all()
			.assertThat()
			.statusCode(200)
			.body("msg", equalTo("Address successfully updated"))
			.header("server", "Apache/2.4.18 (Ubuntu)");
		
		// Get Place
		String getPlaceResponse = given()
									.log().all()
									.baseUri("https://rahulshettyacademy.com")
									.queryParam("key", "qaclick123")
									.queryParam("place_id", placeId)
								 .when()
								 	.log().all()
								 	.get("maps/api/place/get/json")
								 .then()
								 	.log().all()
								 	.assertThat()
								 	.body("language", equalTo("French-IN"))
								 	.statusCode(200)
								 	.extract().response().asString();
		
		JsonPath js1 = ReusableMethods.rawToJason(getPlaceResponse);
		String actualAddress = js1.getString("address");
		
		System.out.println(actualAddress);
		
		// Cucumber Junit, TestNG
		
		Assert.assertEquals(actualAddress, newAddress);
		
	}

}
