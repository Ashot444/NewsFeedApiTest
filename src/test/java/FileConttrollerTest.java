import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileConttrollerTest {

    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();


    @Epic("Positive file")
    @Feature("Positive post file")
    @Test
    public void fileControllerTest() {

        request.header("Content-Type", "multipart/form-data").multiPart(new File("src/main/resources/img.jpeg"));

        Response response = request.post(routes.fileUpload).
                then().contentType(ContentType.JSON).log().all().extract().response();

        int statusCode = response.getStatusCode();

        String success = response.jsonPath().getString("success");

        softAssertions.assertThat(200).isEqualTo(statusCode);
        softAssertions.assertThat("true").isEqualTo(success);

        softAssertions.assertAll();
    }

    @Epic("Positive file")
    @Feature("Positive get file")
    @Test
    public void fileGetTest() {
        request.header("Content-Type", "application/json");

        Response response = request.queryParam("fileName").get(routes.getFile + "eeca6661-85a4-4cd1-b53d-fecec44585ab.jpeg").
                then().contentType(ContentType.JSON).log().body().extract().response();

        int statusCode = response.getStatusCode();

        softAssertions.assertThat(200).isEqualTo(statusCode);

        softAssertions.assertAll();
    }
}
