import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Methods {

    SoftAssertions softAssertions = new SoftAssertions();
    static Routes routes = new Routes();

    ErrorCode errorCode = new ErrorCode();
    static JSONObject requestBody = new JSONObject();
    static RequestSpecification request = RestAssured.given();


    //registration
    static String correctRegAvatar = generateRandomHexString(5);

    static String correctPassword =  "123456";

    static String correctRegName = generateRandomHexString(5);

    static String correctEmail = "default@email.com";

    static String correctRegRole = "user";


    //createNews

    static String description = Methods.generateRandomHexString(5);
    static String image = Methods.generateRandomHexString(5);
    static ArrayList<String> tags = new ArrayList<>();
    static String title = Methods.generateRandomHexString(5);

    public static String generateRandomHexString (int length){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < length){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, length);
    }

    public static void showBodyPostLogin(RequestSpecification request, Routes routes) {
        request.post(routes.postLogin).then().
                contentType(ContentType.JSON).extract().response().prettyPrint();
    }

    public static Response login(){
        requestBody.put("email", correctEmail);
        requestBody.put("password", correctPassword);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(routes.postLogin).then().
                contentType(ContentType.JSON).log().all().extract().response();

        return response;
    }

    public static Response registration(){
        RequestSpecification request = RestAssured.given();

        requestBody.put("avatar", correctRegAvatar );
        requestBody.put("email", correctEmail);
        requestBody.put("name", correctRegName);
        requestBody.put("password", correctPassword);
        requestBody.put("role",  correctRegRole);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post(routes.postRegistration).then().
                contentType(ContentType.JSON).log().body().extract().response();

        return response;
    }

    public static Response createNewsTest(String token){
        tags.add(Methods.generateRandomHexString(3));
        tags.add(Methods.generateRandomHexString(3));
        tags.add(Methods.generateRandomHexString(3));

        requestBody.put("description", description );
        requestBody.put("image", image);
        requestBody.put("tags", tags);
        requestBody.put("title", title);

        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.auth().oauth2(token).post(routes.createNews).then().
                contentType(ContentType.JSON).log().all().extract().response();

        return response;
    }

    public static Response deleteUser(String token){
        Response response = request.auth().oauth2(token).delete(routes.deleteUser).
                then().contentType(ContentType.JSON).log().all().extract().response();

        return response;
    }
}
