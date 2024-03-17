import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class testGetResponse {
    @Test
    public void testGetResponseFromTestSite()
    {
        String location = "https://playground.learnqa.ru/api/long_redirect";
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get(location)
                .andReturn();

        int statusCode = response.getStatusCode();


        while (statusCode != 200){
           location = response.getHeader("Location");
           System.out.println("Status code " + statusCode + "."+ " Location is " + location);
           response = RestAssured
                   .given()
                   .redirects()
                   .follow(false)
                   .get(location)
                   .andReturn();
           statusCode = response.statusCode();
        }

        System.out.println("Status code " + statusCode);
    }
}
