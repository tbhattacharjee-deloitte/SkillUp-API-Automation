package Users;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Users {

    private String token;
    private Logger logger;
    private static RequestSpecification reqSpec;
    private static ResponseSpecification resSpec;


    public Users(String token, Logger logger) {
        this.token = token;
//        this.logger = logger;

        // building reqSpec
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(BaseProp.baseUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token);
        reqSpec = RestAssured.with().spec(reqBuilder.build());

        // building resSpec
        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        resSpec = resBuilder.build();
    }

    public void createUser(){
        JSONObject obj = new JSONObject();

        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/users/?roleName=EMPLOYEE").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());

        System.out.println(res);

    }

    public static void get_all_users(){
        Response response = given().spec(reqSpec).
                get("/api/users/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;

        JSONArray jsonArray = new JSONArray(response.asString());

        System.out.println(jsonArray.length());
    }

    public static void get_user_by_ID(int id){
        Response response = given().spec(reqSpec).get("/api/users/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        System.out.println(res);
    }

    public void update_user(){

    }

    public static void get_all_trainings_as_trainee(int id){
        Response response = given().spec(reqSpec).get("/api/users/myTrainings/trainee/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;

        JSONArray jsonArray = new JSONArray(response.asString());
        System.out.println(jsonArray);
    }

    public static void get_all_trainings_as_trainer(int id){
        Response response = given().spec(reqSpec).get("/api/users/myTrainings/trainer/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;

        JSONArray jsonArray = new JSONArray(response.asString());
        System.out.println(jsonArray);
    }
}
