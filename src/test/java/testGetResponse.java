import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class testGetResponse {
    @Test
    public void testGetResponseFromTestSite()
    {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String secondMessage = response.get("messages[1].message");
        System.out.println(secondMessage);
    }
}
