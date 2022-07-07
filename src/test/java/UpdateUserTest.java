import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class UpdateUserTest {
    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();

    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();

    static String newCorrectAvatar = Methods.generateRandomHexString(5);

    static String newCorrectEmail = "default44444@email.com";

    static String newCorrectName = Methods.generateRandomHexString(5);

    static String newCorrectRole = "user";


    @Epic("Positive user")
    @Feature("Positive update user")
    @Test
    public void updateUserTest() {
        Response response = methods.registration();

        requestBody.put("avatar", newCorrectAvatar );
        requestBody.put("email", newCorrectEmail);
        requestBody.put("name", newCorrectName);
        requestBody.put("role",  newCorrectRole);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

         response = request.log().all().auth().oauth2(token).put(routes.updateUser).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

       String avatar = (String)response.jsonPath().getMap("data").get("avatar");
       String email = (String)response.jsonPath().getMap("data").get("email");
       String name = (String)response.jsonPath().getMap("data").get("name");
       String role = (String)response.jsonPath().getMap("data").get("role");
       String success = response.jsonPath().getString("success");


        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(newCorrectAvatar).isEqualTo(avatar);
        softAssertions.assertThat(newCorrectEmail).isEqualTo(email);
        softAssertions.assertThat(newCorrectName).isEqualTo(name);
        softAssertions.assertThat(newCorrectRole).isEqualTo(role);

        softAssertions.assertAll();

        methods.deleteUser(token);
    }

    @Epic("Negative user")
    @Feature("Negative update user")
    @Test
    public void incorrectUpdateUserTest(){
        Response response = methods.registration();

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        requestBody.put("avatar", newCorrectAvatar );
        requestBody.put("email", newCorrectEmail);
        requestBody.put("name", "a");
        requestBody.put("role",  newCorrectRole);

        request.body(requestBody.toString());

        response = request.auth().oauth2(token).put(routes.updateUser).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");

        softAssertions.assertThat(400).isEqualTo(statusCode);
        softAssertions.assertThat("false").isEqualTo(success);

        softAssertions.assertAll();

        methods.deleteUser(token);
    }
}
