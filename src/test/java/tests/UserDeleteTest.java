package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Delete cases")
public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This test doest delete user data from service")
    @DisplayName("Negative case to delete user")
    public void testTryToDeleteUserThatCantDelete() {
        //LOG USER
        Map<String,String> authDate = new HashMap<>();
        authDate.put("email", "vinkotov@example.com");
        authDate.put("password", "1234");
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");
        String userId = String.valueOf(responseGetRegisteredUserData.jsonPath().getInt("user_id"));

        //TRY TO DELETE
        Response tryToDeleteResponse = apiCoreRequests
               .makeDeleteRequestUserWithAuthorizationData("https://playground.learnqa.ru/api/user/",this.header, this.cookie,userId);
        Assertions.asserJsonByName(tryToDeleteResponse, "error", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }
    @Test
    @Description("This test delete user data from service")
    @DisplayName("Positive case to delete user")
    public void testTryToDeleteUser() {
        //CREATE NEW USER
        Map<String,String> userRegistrationData = DataGenerator.getRegistrationDate();
        Response responseRegistration= apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userRegistrationData);
        String userId = responseRegistration.jsonPath().getString("id");

        //LOG USER
        Map<String,String> authDate = new HashMap<>();
        authDate.put("email", userRegistrationData.get("email"));
        authDate.put("password", userRegistrationData.get("password"));
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");

        //TRY TO DELETE
        Response tryToDeleteResponse = apiCoreRequests
                .makeDeleteRequestUserWithAuthorizationData("https://playground.learnqa.ru/api/user/",this.header, this.cookie,userId);
        Assertions.asserJsonByName(tryToDeleteResponse, "success", "!");

        //LOG USER
        Response getRegisteredUserDataToValidateDeleteResponse = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        Assertions.assertResponseTextEquals(getRegisteredUserDataToValidateDeleteResponse, "Invalid username/password supplied");
    }

    @Test
    @Description("User trying to delete another user data")
    @DisplayName("This test trying to delete another user data")
    public void tryingToDeleteAnotherUserData() {
        //GENERATE USER THAT TRY TO DELETE ANOTHER USER DATA
        Map<String, String> userThatTryToDeleteAnotherUserData = DataGenerator.getRegistrationDate();
        Response userThatTryToDeleteAnotherUserDataResponse = apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userThatTryToDeleteAnotherUserData);

        //LOG USER THAT TRY TO DELETE ANOTHER USER DATA
        Map<String, String> authDate = new HashMap<>();
        authDate.put("email", userThatTryToDeleteAnotherUserData.get("email"));
        authDate.put("password", userThatTryToDeleteAnotherUserData.get("password"));
        Response responseGetRegisteredUserData = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authDate);
        this.cookie = this.getCookie(responseGetRegisteredUserData, "auth_sid");
        this.header = this.getHeader(responseGetRegisteredUserData, "x-csrf-token");


        //GENERATE USER THAT GET TRIED TO BE DELETED REGISTRATION DATA
        Map<String, String> UserThatGetTriedToBeDeletedRegistrationData = DataGenerator.getRegistrationDate();
        Response userIdThatGetTriedToBeDeletedRegistrationResponse = apiCoreRequests
                .makePostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", UserThatGetTriedToBeDeletedRegistrationData);
        String userIdThatGetTriedToBeDeletedData = userIdThatGetTriedToBeDeletedRegistrationResponse.jsonPath().getString("id");

        //TRY TO DELETE
        Response tryToDeleteResponse = apiCoreRequests
                .makeDeleteRequestUserWithAuthorizationData("https://playground.learnqa.ru/api/user/",this.header, this.cookie,userIdThatGetTriedToBeDeletedData);
        Assertions.asserJsonByName(tryToDeleteResponse, "error", "This user can only delete their own account.");
    }
}



