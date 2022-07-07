import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UpdateNewsTest {
    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();

    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();

    static String description = Methods.generateRandomHexString(5);
    static String image = Methods.generateRandomHexString(5);
    static ArrayList<String> tags = new ArrayList<>();
    static String title = Methods.generateRandomHexString(5);

    @Epic("Positive news")
    @Feature("Positive update news")
    @Test
    public void updateNewsTest() {
        tags.add(Methods.generateRandomHexString(3));
        tags.add(Methods.generateRandomHexString(3));
        tags.add(Methods.generateRandomHexString(3));

        requestBody.put("description", description );
        requestBody.put("image", image);
        requestBody.put("tags", tags);
        requestBody.put("title", title);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = methods.registration();

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        response = methods.createNewsTest(token);

        String id =  response.jsonPath().getString("id");

        response = request.log().all().auth().oauth2(token).put(routes.updateNews + "/" + id).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");

        softAssertions.assertThat(200).isEqualTo(statusCode);

        softAssertions.assertThat("true").isEqualTo(success);

        softAssertions.assertAll();

        methods.deleteUser(token);
    }

    @Epic("Negative news")
    @Feature("Negative update news")
    @Test
    public void incorrectUpdateNewsTest() {
        tags.add(Methods.generateRandomHexString(3));
        tags.add(Methods.generateRandomHexString(3));
        tags.add(Methods.generateRandomHexString(3));

        requestBody.put("tags", tags);
        requestBody.put("title", title);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = methods.registration();

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        response = methods.createNewsTest(token);

        String id =  response.jsonPath().getString("id");

        response = request.log().all().auth().oauth2(token).put(routes.updateNews + "/" + id).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");

        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(400).isEqualTo(statusCode);

        softAssertions.assertThat("true").isEqualTo(success);

        softAssertions.assertThat(errorCode. NEWS_DESCRIPTION_NOT_NULL).isEqualTo(customStatusCode);

        softAssertions.assertAll();

        methods.deleteUser(token);
    }
}
