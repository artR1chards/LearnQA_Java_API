package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;



public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        System.out.println(responseUserData.asString());
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email","vinkotov@example.com");
        authDate.put("password","1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authDate)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");



        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        String[] expectedFields = {"username","firstName","lastName","email"};
        Assertions.assertJsonHasFields(responseUserData,expectedFields);

    }

    @Test
    @Description("This test show only user name for another user registration data")
    @DisplayName("This test show only user name")

    public void testGetDateAnotherUser(){
        //CREATE NEW USER
        String userId ;
        String newUserName = "";

        Map<String,String> newUserData = DataGenerator.getRegistrationDate();
        newUserName = newUserData.get("username");

        Map<String,String> newAuthDate = new HashMap<>();
        newAuthDate.put("email", newUserData.get("email"));
        newAuthDate.put("password", newUserData.get("password"));

        Response responseGetAuthNewUserData = apiCoreRequests
                .makerPostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", newUserData);

        userId  = responseGetAuthNewUserData.jsonPath().getString("id");

        //LOGIN WITH REGISTERED USER

        Map<String,String> authDate = new HashMap<>();
        authDate.put("email","vinkotov@example.com");
        authDate.put("password", "1234");
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);

        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");

        //TRT RO GET NEW AUTH USER DATA BY REGISTERED USER

        Response getData = apiCoreRequests.
                getUserData("https://playground.learnqa.ru/api/user/", this.header, this.cookie, userId);

        Assertions.asserJsonByName(getData,"username", newUserName);
        String[] expectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonHasNoTFields(getData, expectedFields);
    }
}


