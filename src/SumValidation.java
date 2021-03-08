import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {

	@Test
	public void sumOfCourses()
	{
		int sum = 0;
		JsonPath js = new JsonPath(payload.CoursePrice());
		int count=	js.getInt("courses.size()");
		for(int i=0;i<count;i++)
		{
			String courseName = js.getString("courses["+i+"].title");
			System.out.println("Course Name: " + courseName);
			int price=js.getInt("courses["+i+"].price");
			System.out.println("Price/unit: " + price);
			int copies=js.getInt("courses["+i+"].copies");
			System.out.println("Copies: " + copies);
			int amount = price * copies;
			System.out.println("Total price:" + amount);
			sum = sum + amount;
		}
		System.out.println("Sum of Total Price: " + sum);
		int purchaseAmount =js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(sum, purchaseAmount);
		
	}
}