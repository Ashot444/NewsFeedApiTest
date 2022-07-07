import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class SearchNewsOneTest {
    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();

    @Epic("Positive news")
    @Feature("Positive search news one")
    @Test
    public void searchNewsOneTest() {
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.queryParam("page", "1").queryParam("perPage", "1").get(routes.searchOneNews).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertAll();
    }

    @Epic("Negative news")
    @Feature("Negative search news one")
    @Test
    public void incorrectSearchNewsOneTest() {
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.queryParam("page", "0").queryParam("perPage", "1").get(routes.searchOneNews).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        softAssertions.assertThat(400).isEqualTo(statusCode);
        softAssertions.assertAll();

    }
}
