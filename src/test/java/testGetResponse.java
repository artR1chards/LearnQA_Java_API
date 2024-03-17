import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class testGetResponse {
    @Test
    public void testGetResponseFromTestSite()
    {

        String location = "https://playground.learnqa.ru/ajax/api/longtime_job";
        Map<String,String> tokenParam = new HashMap<>();
        String jobIsNotReadyStatus = "Job is NOT ready";
        String jobIsDoneStatus = "Job is ready" ;
        String errorStatus = "No job linked to this token";
        int waitTime = 0;

//  создается задача
        JsonPath response = RestAssured
                .get(location)
                .jsonPath();
        tokenParam.put("token", response.get("token"));
        waitTime = response.get("seconds");
// делается один запрос с token ДО того, как задача готова, убеждаемся в правильности поля status
        response = RestAssured
                .given()
                .queryParams(tokenParam)
                .get(location)
                .jsonPath();
// проверка на статусы
        if (response.get("error") != null && response.get("error").equals("No job linked to this token")) {
            System.out.println("status is correct " + errorStatus);
        }
        else{
            if (response.get("status").equals(jobIsNotReadyStatus)){
                System.out.println("status is correct " + jobIsNotReadyStatus);
                while (response.get("status").equals(jobIsNotReadyStatus)){
                    try {
                        Thread.sleep(waitTime * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    response = RestAssured
                            .given()
                            .queryParams(tokenParam)
                            .get(location)
                            .jsonPath();
                }

            }
            if (response.get("status").equals(jobIsDoneStatus) && response.get("result") != null){
                System.out.println("status is correct " + jobIsDoneStatus);
                System.out.println("result is check and has value " + response.get("result"));
            }
        }
    }
}
