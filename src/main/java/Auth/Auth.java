package Auth;

import Base.BaseProp;
import com.aventstack.extentreports.ExtentTest;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class Auth {
    public static String login(String username, String password) {
        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification reqSpec;
        ResponseSpecification resSpec;

        // building reqSpec
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(BaseProp.baseUrl)
                .addHeader("Content-Type", "application/json");
        reqSpec = RestAssured.with().spec(reqBuilder.build());

        // building resSpec
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        resSpec = resBuilder.build();

        //getting response
        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("password", password);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/generate-token").then().spec(resSpec).extract().response();

        JSONObject res = new JSONObject(response.asString());
        return res.get("token").toString();
    }
}
