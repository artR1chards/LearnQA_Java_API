import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class checkHeader {
    @Test
    public void checkCookieValue() {
        Map<String, String> validCookie = new HashMap<>();
        validCookie.put("HomeWork", "hw_value");


        Response responseGetCookies = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String, String> cookies = responseGetCookies.getCookies();
        assertTrue(cookies.equals(validCookie), "Response doesn't have correctCookie");
    }
}
