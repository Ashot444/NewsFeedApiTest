import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class DeleteNewsTest {
    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();


    @Epic("Positive news")
    @Feature("Positive delete news")
    @Test
    public void deleteNewsTest() {
        Response response = methods.registration();

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        response = methods.createNewsTest(token);

        String id =  response.jsonPath().getString("id");

         response = request.auth().oauth2(token).delete(routes.deleteNews + "/" + id).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");

        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);

        methods.deleteUser(token);
    }

    @Epic("Negative news")
    @Feature("Negative delete news")
    @Test
    public void incorrectDeleteNewsTest() {
        Response response = methods.registration();

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        methods.createNewsTest(token);

        String id =  "5679";

        response = request.auth().oauth2(token).delete(routes.deleteNews + "/" + id).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(400).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(errorCode.NEWS_NOT_FOUND).isEqualTo(customStatusCode);

        methods.deleteUser(token);
    }

}
