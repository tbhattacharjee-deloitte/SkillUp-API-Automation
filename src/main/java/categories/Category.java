package categories;

import Base.BaseProp;
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


import static io.restassured.RestAssured.given;

public class Category {
    private final String token;
    private final Logger logger;
    private final ResponseSpecification resSpec;
    private final RequestSpecification reqSpec;
    private int createdCategoryId;
    private int createdSkillId;

    public int getCreatedCategoryId() {
        return createdCategoryId;
    }

    public Category(String token, Logger logger) {
        this.token = token;
        this.logger = logger;


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

    public void createCategory(String categoryName) {
        JSONObject obj = new JSONObject();
        obj.put("categoryName", categoryName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/categories/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        logger.debug(res.toString());
        this.createdCategoryId = Integer.parseInt(res.get("categoryId").toString());
    }

    public void getAllCategory() {
        Response response = given().spec(reqSpec).get("api/categories/").then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONArray arr = new JSONArray(response.asString());
        logger.debug(arr);
    }

    public void getCategoryById(int id) {
        Response response = given().spec(reqSpec).get("api/categories/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        logger.debug("Skill data with id = {" + id + "} => " + res);
    }

    public void getCategoryByName(String name) {
        Response response = given().spec(reqSpec).param("categoryName", name).
                get("/api/categories/name_category").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        logger.debug(res);
    }

    public void updateCategory(int id, String categoryName) {
        getCategoryById(id);
        JSONObject obj = new JSONObject();
        obj.put("categoryName", categoryName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .put("/api/categories/update/" + id).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        logger.debug("Updated skill is " + response.asString());
    }

    public void createSkill(String skillName) {
        JSONObject obj = new JSONObject();
        obj.put("skillName", skillName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/skills/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        logger.debug(res.toString());
        this.createdSkillId = Integer.parseInt(res.get("skillId").toString());
    }

    public void addSkillToCategory(int id) {
        getCategoryById(id);
        Response response = given().spec(reqSpec)
                .put("/api/categories/addSkill/"+id+"/"+createdSkillId).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        logger.debug("skill added is " + response.asString());
    }

    public void DeleteCategoryById(int id) {
        Response response = given().spec(reqSpec)
                .delete("/api/categories/" + id).then()
                .extract().response();
        assert response.statusCode() == BaseProp.OK;
        System.out.println("category deleted");
    }





}
