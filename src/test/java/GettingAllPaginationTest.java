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

public class GettingAllPaginationTest {

    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();

    @Epic("Positive news")
    @Feature("Positive news pagination")
    @Test
    public void getAllPaginationTest() {
        int newsCount = request
                .contentType(ContentType.JSON)
                .queryParam("page","1")
                .queryParam("perPage", "1")
                .when()
                .get(routes.paginationNews)
                .then().log().body()
                .extract().response().path("data.numberOfElements");
        int i = (int) Math.ceil(newsCount / 100.0);
        int NewsAmount = 0;
        while (i > 0) {
            List<NewsId> nArr = request
                    .contentType(ContentType.JSON)
                    .queryParam("page", i)
                    .queryParam("perPage", "1")
                    .when()
                    .get(routes.paginationNews)
                    .then()
                    .extract().jsonPath().getList("data.content", NewsId.class);
            NewsAmount += nArr.size();
            System.out.println(nArr);
            i--;
        }
        Response response = request
                .contentType(ContentType.JSON)
                .queryParam("page", "1")
                .queryParam("perPage", "1")
                .when()
                .get(routes.paginationNews)
                .then().log().all()
                .extract().response();

        int statusCode = response.getStatusCode();
        String success = response.jsonPath().getString("success");
        int customStatusCode = response.jsonPath().getInt("statusCode");
        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);
        softAssertions.assertThat(Math.ceil(newsCount / 100.0)).isEqualTo(NewsAmount);
        softAssertions.assertAll();
    }
}
