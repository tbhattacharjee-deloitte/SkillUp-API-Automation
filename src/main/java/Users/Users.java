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
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;

public class Users {

    private String token;
    private Logger logger;
    private static RequestSpecification reqSpec;
    private static ResponseSpecification resSpec;
    public static int addeduserId;
    public static Properties prop;
    static {
        try{
            prop = new Properties();
            FileInputStream ip=new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\Users\\update_field.properties");
            prop.load(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
        File jsonData = new File("src\\main\\java\\Users\\userdata.json");

        Response response = given().spec(reqSpec).body(jsonData)
                .post("/api/users/?roleName=EMPLOYEE").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        assert (res.get("name").toString()).equals(prop.getProperty("created_user_name1"));

        System.out.println("User with name "+res.get("name")+" created");
        addeduserId = Integer.parseInt(res.get("id").toString());

    }

    public static void get_all_users(){
        Response response = given().spec(reqSpec).
                get("/api/users/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONArray jsonArray = new JSONArray(response.asString());
        assert jsonArray.length() > 0;

        System.out.println("Number of users : "+ jsonArray.length());
    }

    public static void get_user_by_ID(int id){
        Response response = given().spec(reqSpec).get("/api/users/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        assert res.get("id").equals(id);

        System.out.println("Name of user with id : "+res.get("id")+ " is "+res.get("name"));
    }

    public static void update_user(int id,String field,String value){
        get_user_by_ID(id);
        JSONObject obj = new JSONObject();
        obj.put(field, value);
        Response response = given().spec(reqSpec).body(obj.toString())
                .put("/api/users/update/" + id).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        assert ((res.get(field)).toString()).equals(obj.get(field).toString());

        System.out.println(field + " of User with id " +res.get("id")+ " updated to "+res.get(field));

    }

    public static void delete_user(){
        Response response = given().spec(reqSpec)
                .delete("/api/users/" + addeduserId).then()
                .extract().response();
        System.out.println("User with ID : "+addeduserId +" deleted");
        assert response.statusCode() == BaseProp.OK;

    }

    public static void get_all_trainings_as_trainee(int id){
        Response response = given().spec(reqSpec).get("/api/users/myTrainings/trainee/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;

        JSONArray jsonArray = new JSONArray(response.asString());
        System.out.println("Number of trainings as trainee :"+jsonArray.length());
    }

    public static void get_all_trainings_as_trainer(int id){
        Response response = given().spec(reqSpec).get("/api/users/myTrainings/trainer/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;

        JSONArray jsonArray = new JSONArray(response.asString());
        System.out.println("Number of trainings as trainer :"+jsonArray.length());
    }
}
