package categories;

import Base.BaseProp;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONArray;
import org.json.JSONObject;


import static io.restassured.RestAssured.given;

public class Category {
    private final String token;
    private final ResponseSpecification resSpec;
    private final RequestSpecification reqSpec;
    public int createdCategoryId;
    public int createdSkillId;


    //constructor for category class
    public Category(String token) {
        this.token = token;

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

    //creating a category
    public void  createCategory(String categoryName) {
        JSONObject obj = new JSONObject();
        obj.put("categoryName", categoryName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/categories/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        System.out.println("CATEGORY CREATED IS:" + res);
        this.createdCategoryId = Integer.parseInt(res.get("categoryId").toString());
    }

    //getting all the category
    public void getAllCategory() {
        Response response = given().spec(reqSpec).get("api/categories/").then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONArray arr = new JSONArray(response.asString());
        System.out.println("ALL CATEGORY IS:" + arr);
    }

    //getting category by id
    public void getCategoryById(int id) {
        Response response = given().spec(reqSpec).get("api/categories/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        System.out.println("SKILL DATA WITH ID = {" + id + "} => " + res);
    }

    //getting category by name
    public void getCategoryByName(String name) {
        Response response = given().spec(reqSpec).param("categoryName", name).
                get("/api/categories/name_category").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        System.out.println("CATEGORY BY NAME IS:" + res);
    }

    //updating a category
    public void updateCategory(int id, String categoryName) {
        getCategoryById(id);
        JSONObject obj = new JSONObject();
        obj.put("categoryName", categoryName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .put("/api/categories/update/" + id).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        System.out.println("UPDATED SKILL IS: " + response.asString());
    }

    //creating a new skill
    public void createSkill(String skillName) {
        JSONObject obj = new JSONObject();
        obj.put("skillName", skillName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/skills/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        System.out.println("CREATED SKILL IS:" + res);
        this.createdSkillId = Integer.parseInt(res.get("skillId").toString());
    }

    //adding skill to category
    public void addSkillToCategory(int id) {
        getCategoryById(id);
        Response response = given().spec(reqSpec)
                .put("/api/categories/addSkill/"+id+"/"+createdSkillId).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        System.out.println("SKILL ADDED IS: " + response.asString());
    }

    //deleting a category by its id
    public void DeleteCategoryById(int id) {
        Response response = given().spec(reqSpec)
                .delete("/api/categories/" + id).then()
                .extract().response();
        assert response.statusCode() == BaseProp.OK;
        System.out.println("CATEGORY SUCCESSFULLY DELETED");
    }





}
