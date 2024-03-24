import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class checkHeader {
    String header = "";
    String url = "";
    @Test
    public void checkHeaderValue() {
        String expectedHeaderValue = "Some secret value";
        this.url = "https://playground.learnqa.ru/api/homework_header";
        Response responseGetHeader = RestAssured
                .given()
                .get(this.url)
                .andReturn();
        this.header = responseGetHeader.header("X-Secret-Homework-Header");
        assertTrue(this.header.equals(expectedHeaderValue), "There is not correct header");
    }
}
