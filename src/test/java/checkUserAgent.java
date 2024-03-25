import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class checkUserAgent {
    String url = "";
    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(new Object[]{"Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30","Mobile","No","Android" }),
                Arguments.of(new Object[]{"Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1","Mobile","Chrome","iOS"}),
                Arguments.of(new Object[]{"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)","Googlebot","Unknown","Unknown"}),
                Arguments.of(new Object[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0","Web","Chrome","No"}),
                Arguments.of(new Object[]{"Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1","Mobile","No","iPhone"}));

    }
    @ParameterizedTest
    @MethodSource({"dataProvider"})
    public void checkResponse(String userAgent,String expectedPlatform,String expectedBrowser, String expectedDevice) {
        this.url = "https://playground.learnqa.ru/ajax/api/user_agent_check";
        JsonPath responseCheckBody = RestAssured
                .given()
                .header("User-Agent", userAgent)
                .get(this.url)
                .jsonPath();

        assertTrue(responseCheckBody.getString("platform").equals(expectedPlatform), "Wrong platform");
        assertTrue(responseCheckBody.getString("browser").equals(expectedBrowser), "Wrong browser");
        assertTrue(responseCheckBody.getString("device").equals(expectedDevice), "Wrong device");
    }
}
