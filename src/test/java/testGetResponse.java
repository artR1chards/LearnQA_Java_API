import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class testGetResponse {
    @Test
    public void testGetResponseFromTestSite()
    {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String headerLocation = response.getHeader("Location");
        System.out.println("Location is " + headerLocation);
    }
}
