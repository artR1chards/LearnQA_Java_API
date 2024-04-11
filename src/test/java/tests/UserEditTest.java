package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
@Epic("Edition cases")
public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testEditJustCreatedTest(){
        //GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationDate();
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email", userData.get("email"));
        authDate.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authDate)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT

        String newName = "Change Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth,"auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.asserJsonByName(responseUserData,"firstName", newName);
    }

    @ParameterizedTest
    @Description("This test doesn't change user data")
    @DisplayName("This test doesn't change user data without authorization data")
    @ValueSource(strings = {"cookie", "token", "without cookie and token"})
    public void changeUserDataWithoutCsrfToken(String condition){
        //GENERATE USER
        Map<String,String> userRegistrationData = DataGenerator.getRegistrationDate();
        Response responseRegistration= apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userRegistrationData);
        String userId = responseRegistration.jsonPath().getString("id");

        //DATA FOT EDITION
        Map<String,String>  userDateForEdition = new HashMap<>();
        userDateForEdition.put("username","someUserName");
        userDateForEdition.put("firstName","someFirstName");
        userDateForEdition.put("lastName","someLastName");
        userDateForEdition.put("email","something@test.com");
        userDateForEdition.put("password","somePass");

        Map<String, String> dateForEdition = DataGenerator.getRegistrationDate(userDateForEdition);

        //LOG USER
        Map<String,String> authDate = new HashMap<>();
        authDate.put("email", userRegistrationData.get("email"));
        authDate.put("password", userRegistrationData.get("password"));
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");

        //EDIT
        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makePutRequestForEditionWithOnlyCookie("https://playground.learnqa.ru/api/user/", userId, this.cookie,dateForEdition);
            Assertions.asserJsonByName(responseForCheck,"error","Auth token not supplied");
        } else if (condition.equals("token")) {
            Response responseForCheck = apiCoreRequests.makePutRequestForEditionWithOnlyCsrfToken("https://playground.learnqa.ru/api/user/", userId,this.header,dateForEdition);
            Assertions.asserJsonByName(responseForCheck,"error","Auth token not supplied");
        } else if (condition.equals("without cookie and token")) {
            Response responseForCheck = apiCoreRequests.makePutRequestForEditionWithoutAuthorizationData("https://playground.learnqa.ru/api/user/", userId, dateForEdition);
            Assertions.asserJsonByName(responseForCheck,"error","Auth token not supplied");
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
    }

    @Test
    @Description("User trying to change another user data")
    @DisplayName("This test trying to change another user data")
    public void tryingToEditAnotherUserData() {
        //GENERATE USER THAT TRY TO CHANGE ANOTHER USER DATA
        Map<String, String> userThatTryToChangeAnotherUserDataRegistrationData = DataGenerator.getRegistrationDate();
        Response userThatTryToChangeAnotherUserDataRegistrationDataRegistrationResponse = apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userThatTryToChangeAnotherUserDataRegistrationData);

        //LOG USER THAT TRY TO CHANGE ANOTHER USER DATA
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email", userThatTryToChangeAnotherUserDataRegistrationData.get("email"));
        authDate.put("password", userThatTryToChangeAnotherUserDataRegistrationData.get("password"));
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");

        //DATA FOT EDITION
        Map<String,String>  userDateForEdition = new HashMap<>();
        userDateForEdition.put("username","someUserName");
        userDateForEdition.put("firstName","someFirstName");
        userDateForEdition.put("lastName","someLastName");
        userDateForEdition.put("email","something@test.com");
        userDateForEdition.put("password","somePass");

        Map<String, String> dateForEdition = DataGenerator.getRegistrationDate(userDateForEdition);

        //GENERATE USER THAT GET TRIED TO BE CHANGED REGISTRATION DATA
        Map<String, String> UserThatGetTriedToBeChangedRegistrationData = DataGenerator.getRegistrationDate();
        Response userIdThatGetTriedToBeChangedRegistrationResponse = apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", UserThatGetTriedToBeChangedRegistrationData);
        String userIdThatGetTriedToBeChangedRegistrationData = userIdThatGetTriedToBeChangedRegistrationResponse.jsonPath().getString("id");

        Response responseForCheck = apiCoreRequests.makePutRequestForEditionWithAuthorizationData("https://playground.learnqa.ru/api/user/", userIdThatGetTriedToBeChangedRegistrationData, this.cookie, this.header, dateForEdition);
        Assertions.asserJsonByName(responseForCheck, "error", "This user can only edit their own data.");
    }

    @Test
    @Description("Trying to change email on Invalid email")
    @DisplayName("Invalid email")
    public void tryingToEditEmailOnInvalidEmail() {
        //GENERATE USER THAT TRY TO CHANGE EMAIL ON WRONG EMAIL
        Map<String, String> userThatTryToEditEmailOnInvalidEmailDate = DataGenerator.getRegistrationDate();
        Response userThatTryToEditEmailOnInvalidEmailDateResponse = apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userThatTryToEditEmailOnInvalidEmailDate);
        String userIdThatTryToEditEmailOnInvalidEmailDate = userThatTryToEditEmailOnInvalidEmailDateResponse.jsonPath().getString("id");

        //GENERATE WRONG EMAIL DATE
        Map<String,String>  wrongEmailDate = new HashMap<>();
        wrongEmailDate.put("email","somethingtest.com");

        //LOG USER THAT TRY TO CHANGE EMAIL ON WRONG EMAIL
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email", userThatTryToEditEmailOnInvalidEmailDate.get("email"));
        authDate.put("password", userThatTryToEditEmailOnInvalidEmailDate.get("password"));
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");

        // TRY TO CHANGE EMAIL ON WRONG EMAIL
        Response responseForCheck = apiCoreRequests.makePutRequestForEditionWithAuthorizationData("https://playground.learnqa.ru/api/user/", userIdThatTryToEditEmailOnInvalidEmailDate, this.cookie, this.header, wrongEmailDate);
        Assertions.asserJsonByName(responseForCheck, "error", "Invalid email format");
    }

    @Test
    @Description("Trying to change firstName on invalid firstName")
    @DisplayName("Invalid firstName")
    public void tryingToEditFirstNameOnInvalidFirstName  () {
        //GENERATE USER THAT TRY TO CHANGE FIRST NAME ON WRONG FIRST NAME
        Map<String, String> userToEditFirstNameOnInvalidFirstNameDate = DataGenerator.getRegistrationDate();
        Response userToEditFirstNameOnInvalidFirstNameDateResponse = apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userToEditFirstNameOnInvalidFirstNameDate);
        String userIdToEditFirstNameOnInvalidFirstNameDate = userToEditFirstNameOnInvalidFirstNameDateResponse.jsonPath().getString("id");

        //GENERATE WRONG EMAIL DATE
        Map<String,String>  wrongFirstNameDate = new HashMap<>();
        wrongFirstNameDate.put("firstName","s");

        //LOG USER THAT TRY TO CHANGE FIRST NAME ON WRONG FIRST NAME
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email", userToEditFirstNameOnInvalidFirstNameDate.get("email"));
        authDate.put("password", userToEditFirstNameOnInvalidFirstNameDate.get("password"));
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");


        // TRY TO CHANGE FIRST NAME ON WRONG FIRST NAME
        Response responseForCheck = apiCoreRequests.makePutRequestForEditionWithAuthorizationData("https://playground.learnqa.ru/api/user/", userIdToEditFirstNameOnInvalidFirstNameDate, this.cookie, this.header, wrongFirstNameDate);
        Assertions.asserJsonByName(responseForCheck, "error", "The value for field `firstName` is too short");
    }

}
