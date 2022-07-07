import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class LoginPageGetTest {

    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();

    static String correctPassword =  "123456";

    static String correctEmail = "default@email.com";

    @Epic("login")
    @Feature("Positive test")
    @Test
    public void loginTest() {
        methods.registration();
        requestBody.put("email", correctEmail);
        requestBody.put("password", correctPassword);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(routes.postLogin).then().
                contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String email = (String) response.jsonPath().getMap("data").get("email");
        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertThat(methods.correctEmail).isEqualTo(email);
        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(errorCode.USERNAME_SIZE_NOT_VALID).isEqualTo(customStatusCode);

        softAssertions.assertAll();

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        methods.deleteUser(token);
    }

    @Epic("Negative login")
    @Feature("Negative test password")
    @Test
    public void notPassLoginTest(){
        requestBody.put("email", correctEmail);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(routes.postLogin).then().
                contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(400).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(errorCode.PASSWORD_NOT_VALID).isEqualTo(customStatusCode);
        softAssertions.assertAll();
    }

    @Epic("Negative login")
    @Feature("Negative test email")
    @Test
    public void incorrectEmailLoginTest(){

        requestBody.put("email", "ashot234");
        requestBody.put("password", correctPassword);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(routes.postLogin).then().
                contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(400).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(errorCode.USER_EMAIL_NOT_VALID).isEqualTo(customStatusCode);
        softAssertions.assertAll();
    }

}
