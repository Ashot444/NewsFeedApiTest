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

public class RegistrationPageGetTest {
    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();

    //registration
    static String correctRegAvatar = Methods.generateRandomHexString(5);

    static String correctPassword =  "123456";

    static String correctRegName = Methods.generateRandomHexString(5);

    static String correctEmail = "default@email.com";

    static String correctRegRole = "user";



    @Epic("Positive user")
    @Feature("Positive registration user")
    @Test
    public void registrationTest() {
        requestBody.put("avatar", correctRegAvatar );
        requestBody.put("email", correctEmail);
        requestBody.put("name", correctRegName);
        requestBody.put("password", correctPassword);
        requestBody.put("role",  correctRegRole);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(routes.postRegistration).then().
                contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String avatar = (String) response.jsonPath().getMap("data").get("avatar");
        String email = (String) response.jsonPath().getMap("data").get("email");
        String name = (String) response.jsonPath().getMap("data").get("name");
        String role = (String) response.jsonPath().getMap("data").get("role");

        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertThat(correctRegAvatar).isEqualTo(avatar);
        softAssertions.assertThat(correctEmail).isEqualTo(email);
        softAssertions.assertThat(correctRegName).isEqualTo(name);
        softAssertions.assertThat(correctRegRole).isEqualTo(role);

        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(errorCode.USERNAME_SIZE_NOT_VALID).isEqualTo(customStatusCode);

        softAssertions.assertAll();

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        methods.deleteUser(token);
    }

    @Epic("Negative user")
    @Feature("Negative registration user")
    @Test
    public void incorrectEmailRegistrationTest() {
        requestBody.put("avatar", correctRegAvatar );
        requestBody.put("email", "ashot12345");
        requestBody.put("name", correctRegName);
        requestBody.put("password", correctPassword);
        requestBody.put("role",  correctRegRole);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(routes.postRegistration).then().
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
