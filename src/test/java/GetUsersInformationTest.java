import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GetUsersInformationTest {
    SoftAssertions softAssertions = new SoftAssertions();
    Routes routes = new Routes();
    ErrorCode errorCode = new ErrorCode();
    Methods methods = new Methods();
    JSONObject requestBody = new JSONObject();
    RequestSpecification request = RestAssured.given();

    @Epic("Positive user")
    @Feature("Positive get user")
    @Test
    public void getUserInfoTest(){

        Response response = methods.registration();


        List<UsersData> users =  request.get(routes.userAllInfo).then().
                contentType(ContentType.JSON).extract().body().jsonPath().getList("data", UsersData.class);

        users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getName())));

        String token = (String) response.jsonPath().getMap("data").get("token");
        token = token.substring(7);

        methods.deleteUser(token);
    }

    @Epic("Negative user")
    @Feature("Negative get user")
    @Test
    public void incorrectGetUserInfoTest(){

        String id = "3243536";

        Response response = request.get(routes.userAllInfo + id).then().
                contentType(ContentType.JSON).log().all().extract().response();

        int customStatusCode = response.jsonPath().getInt("statusCode");

        softAssertions.assertThat(errorCode.MAX_UPLOAD_SIZE_EXCEEDED).isEqualTo(customStatusCode);
    }

}
