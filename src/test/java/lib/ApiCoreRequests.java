package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid",cookie)
                .get(url)
                .andReturn();
    }
    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token",token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-request")
    public  Response makePostRequest(String url, Map<String, String> authDate){
        return given()
                .filter(new AllureRestAssured())
                .body(authDate)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-request to registration user")
    public  Response makePostRequestToRegistrationUser(String url, Map<String,String> userData){
        return given()
                .body(userData)
                .post(url)
                .andReturn();
    }

    @Step("Get User Data")
    public Response getUserData(String url, String csrfTokenHeader, String cookie, String userId){
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token",csrfTokenHeader)
                .cookie("auth_sid",cookie)
                .get( url + userId)
                .andReturn();
    }

    @Step("Make a Put-edition request with authorization data")
    public  Response makePutRequestForEditionWithAuthorizationData (String url,String userId, String cookie, String csrfTokenHeader, Map<String,String> dataToEdition){
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid",cookie)
                .header("x-csrf-token",csrfTokenHeader)
                .body(dataToEdition)
                .put(url + userId)
                .andReturn();
    }

    @Step("Make a Put-edition request with only cookie")
    public  Response makePutRequestForEditionWithOnlyCookie(String url,String userId, String cookie, Map<String,String> dataToEdition){
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid",cookie)
                .body(dataToEdition)
                .put(url + userId)
                .andReturn();
    }

    @Step("Make a Put-edition request with only x-csrf-token")
    public  Response makePutRequestForEditionWithOnlyCsrfToken(String url,String userId, String csrfTokenHeader, Map<String,String> dataToEdition){
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token",csrfTokenHeader)
                .body(dataToEdition)
                .put(url + userId)
                .andReturn();
    }

    @Step("Make a Put-edition request without authorization data")
    public  Response makePutRequestForEditionWithoutAuthorizationData (String url,String userId, Map<String,String> dataToEdition){
        return given()
                .filter(new AllureRestAssured())
                .body(dataToEdition)
                .put(url + userId)
                .andReturn();
    }

    @Step("Make a Delete request with authorization data")
    public  Response makeDeleteRequestUserWithAuthorizationData(String url, String csrfTokenHeader, String cookie, String userId){
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token",csrfTokenHeader)
                .cookie("auth_sid",cookie)
                .delete( url + userId)
                .andReturn();
    }
}