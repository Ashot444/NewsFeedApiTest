import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class DeleteUserTest {

    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();


    @Epic("Positive user")
    @Feature("Positive delete user")
    @Test
    public void deleteUserTest() {
        Response response =  methods.registration();

         String token = (String) response.jsonPath().getMap("data").get("token");
         token = token.substring(7);

         response = request.auth().oauth2(token).delete(routes.deleteUser).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(errorCode.USERNAME_SIZE_NOT_VALID).isEqualTo(customStatusCode);

        softAssertions.assertAll();
    }

    @Epic("Negative user")
    @Feature("Negative delete user")
    @Test
    public void incorrectDeleteUserTest() {
        String token = "ERROR";

        Response response = request.auth().oauth2(token).delete(routes.deleteUser).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");

        softAssertions.assertThat(401).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);

        softAssertions.assertAll();
    }
}
