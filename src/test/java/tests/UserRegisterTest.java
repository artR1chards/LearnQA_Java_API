package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
@Epic("Registration cases")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationDate(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400 );
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully(){
        String email = DataGenerator.getRandomEmail();
        Map<String,String> userData = DataGenerator.getRegistrationDate();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth,"id");
    }

    @Test
    @Description("This test doesn't register user by email without dot")
    @DisplayName("Registration with email without dot")
    public  void testCreateUserWithEmailWhitOutDot(){
        String wrongEmail = "testwrong.com";
        Map<String,String> userData = new HashMap<>();
        userData.put("email", wrongEmail);
        userData = DataGenerator.getRegistrationDate(userData);

        Response responseGetAuth = apiCoreRequests
                .makerPostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userData);

        Assertions.assertResponseTextEquals(responseGetAuth, "Invalid email format");

    }

    @ParameterizedTest
    @Description("This test checks registration without required field")
    @DisplayName("Test negative user registrations")
    @ValueSource(strings = {"email","password","username","firstName","lastName"})
    public void testNegativeRegistrations(String condition){
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user");

        if(condition.equals("email")) {
            Response responseForCheck = apiCoreRequests.makerPostRequestToRegistrationUser(
                    "https://playground.learnqa.ru/api/user",
                    DataGenerator.getInvalidRegistrationDate("email")
            );
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: email");
        }else if (condition.equals("password")){
            Response responseForCheck = apiCoreRequests.makerPostRequestToRegistrationUser(
                    "https://playground.learnqa.ru/api/user",
                    DataGenerator.getInvalidRegistrationDate("password")
                );
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: password");
        }else if (condition.equals("username")){
            Response responseForCheck = apiCoreRequests.makerPostRequestToRegistrationUser(
                    "https://playground.learnqa.ru/api/user",
                    DataGenerator.getInvalidRegistrationDate("username")
            );
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: username");
        }else if (condition.equals("firstName")){
            Response responseForCheck = apiCoreRequests.makerPostRequestToRegistrationUser(
                    "https://playground.learnqa.ru/api/user",
                    DataGenerator.getInvalidRegistrationDate("firstName")
            );
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: firstName");
        }else if (condition.equals("lastName")){
            Response responseForCheck = apiCoreRequests.makerPostRequestToRegistrationUser(
                    "https://playground.learnqa.ru/api/user",
                    DataGenerator.getInvalidRegistrationDate("lastName")
            );
            Assertions.assertResponseTextEquals(responseForCheck, "The following required params are missed: lastName");
        } else {
            throw new IllegalArgumentException("Codition value is known: " + condition);
        }

    }
    @Test
    @Description("This test doesn't register user with one character name")
    @DisplayName("Registration with one character name")
    public  void testCreateUserWithOneCharacterName(){
        String OneCharacterName = "t";
        Map<String,String> userData = new HashMap<>();
        userData.put("username", OneCharacterName);
        userData = DataGenerator.getRegistrationDate(userData);

        Response responseGetAuth = apiCoreRequests
                .makerPostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userData);
        Assertions.assertResponseTextEquals(responseGetAuth, "The value of 'username' field is too short");
    }

    @Test
    @Description("This test doesn't register user with more than 250 characters name")
    @DisplayName("Registration with more than 250 characters name")
    public  void testCreateUserWithOneSymbolName(){
        String oneSymbolName = "artrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsartrichardsardsartri";
        Map<String,String> userData = new HashMap<>();
        userData.put("username", oneSymbolName);
        userData = DataGenerator.getRegistrationDate(userData);

        Response responseGetAuth = apiCoreRequests
                .makerPostRequestToRegistrationUser("https://playground.learnqa.ru/api/user", userData);
        Assertions.assertResponseTextEquals(responseGetAuth, "The value of 'username' field is too long");
    }

}
