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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Users {

    private String token;
    private Logger logger;
    private static RequestSpecification reqSpec;
    private static ResponseSpecification resSpec;
    private static int addeduserId;


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

    public static void createUser(){
        JSONObject obj = new JSONObject();

        File jsonData = new File("src\\main\\java\\Users\\userdata.json");
        Response response = given().spec(reqSpec).body(jsonData)
                .post("/api/users/?roleName=EMPLOYEE").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());

        System.out.println(res);

        addeduserId = Integer.parseInt(res.get("id").toString());

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

    public static void update_user(int id, int age){
        get_user_by_ID(id);
        JSONObject obj = new JSONObject();
        obj.put("age", age);
        Response response = given().spec(reqSpec).body(obj.toString())
                .put("/api/users/update/" + id).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;

    }

//    public static void add_skill_to_user(int id, String skillName){
//        get_user_by_ID(id);
//        JSONObject obj = new JSONObject();
//        obj.put("skillName", skillName);
//        Response response = given().spec(reqSpec).body(obj.toString())
//                .put("/api/users/addSkill/" + id).then().spec(resSpec).extract().response();
//        assert response.statusCode() == BaseProp.OK;
//
//    }

    public static void delete_user(){
        Response response = given().spec(reqSpec)
                .delete("/api/skills/" + addeduserId).then()
                .extract().response();
        assert response.statusCode() == BaseProp.OK;
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
//
//    public void createSkill(String skillName) {
//        JSONObject obj = new JSONObject();
//        obj.put("skillName", skillName);
//        Response response = given().spec(reqSpec).body(obj.toString())
//                .post("/api/skills/").then().spec(resSpec).extract().response();
//        assert response.statusCode() == BaseProp.OK;
//        JSONObject res = new JSONObject(response.asString());
//        logger.debug(res.toString());
//        this.createdSkillId = Integer.parseInt(res.get("skillId").toString());
//    }
//
//    public void deleteLastCreatedSkill() {
//        Response response = given().spec(reqSpec)
//                .delete("/api/skills/" + this.createdSkillId).then()
//                .extract().response();
//        assert response.statusCode() == BaseProp.OK;
//    }
//
//    public void updateSkills(int id, String skillName) {
//        getSkillByID(id);
//        JSONObject obj = new JSONObject();
//        obj.put("skillName", skillName);
//        Response response = given().spec(reqSpec).body(obj.toString())
//                .put("/api/skills/update/" + id).then().spec(resSpec).extract().response();
//        assert response.statusCode() == BaseProp.OK;
//        logger.debug("Updated skill => " + response.asString());
//    }
