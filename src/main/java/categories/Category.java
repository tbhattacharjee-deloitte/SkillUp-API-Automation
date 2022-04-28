package categories;

import Base.BaseProp;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


import static io.restassured.RestAssured.given;

public class Category {
    private final String token;
    private final Logger logger;
    private final ResponseSpecification resSpec;
    private final RequestSpecification reqSpec;
    public int createdCategoryId;
    public int createdSkillId;


    //constructor for category class
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

    //creating a category
    public void  createCategory(String categoryName) {
        JSONObject obj = new JSONObject();
        obj.put("categoryName", categoryName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/categories/").then().spec(resSpec).extract().response();
        JsonPath jsonPath = new JsonPath(response.asString());
        String categoryName1 = jsonPath.get("categoryName");
        assertThat(categoryName1,equalTo(categoryName));
        assert response.statusCode() == BaseProp.OK;

        JSONObject res = new JSONObject(response.asString());

        logger.debug(res);
        this.createdCategoryId = Integer.parseInt(res.get("categoryId").toString());
    }

    //getting all the category
    public void getAllCategory() {
        Response response = given().spec(reqSpec).get("api/categories/").then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        JSONArray arr = new JSONArray(response.asString());
        System.out.println("ALL CATEGORY IS:" + arr);
    }

    //getting category by id
    public void getCategoryById(int id) {
        Response response = given().spec(reqSpec).get("api/categories/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JsonPath jsonPath = new JsonPath(response.asString());
        int categoryId = jsonPath.getInt("categoryId");
        assertThat(categoryId,equalTo(id));
        JSONObject res = new JSONObject(response.asString());
        logger.debug("SKILL DATA WITH ID = {" + id + "} => " + res);
    }

    //getting category by name
    public void getCategoryByName(String name) {
        Response response = given().spec(reqSpec).param("categoryName", name).
                get("/api/categories/name_category").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JsonPath jsonPath = new JsonPath(response.asString());
        String categoryName1 = jsonPath.get("categoryName");
        assertThat(categoryName1,equalTo(name));
        JSONObject res = new JSONObject(response.asString());
        logger.debug(res);
    }

    //updating a category
    public void updateCategory(int id, String categoryName) {
        getCategoryById(id);
        JSONObject obj = new JSONObject();
        obj.put("categoryName", categoryName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .put("/api/categories/update/" + id).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JsonPath jsonPath = new JsonPath(response.asString());
        String categoryNameUpdated = jsonPath.get("categoryName");
        assertThat(categoryNameUpdated,equalTo(categoryName));
        logger.debug(response.asString());
    }

    //creating a new skill
    public void createSkill(String skillName) {
        JSONObject obj = new JSONObject();
        obj.put("skillName", skillName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/skills/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        JSONObject res = new JSONObject(response.asString());
        logger.debug(res);
        this.createdSkillId = Integer.parseInt(res.get("skillId").toString());
    }

    //adding skill to category
    public void addSkillToCategory(int id) {
        getCategoryById(id);
        Response response = given().spec(reqSpec)
                .put("/api/categories/addSkill/"+id+"/"+createdSkillId).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        logger.debug(response.asString());
    }

    //deleting a category by its id
    public void DeleteCategoryById(int id) {
        Response response = given().spec(reqSpec)
                .delete("/api/categories/" + id).then()
                .extract().response();
        assert response.statusCode() == BaseProp.OK;
        String contentType = response.header("Content-Length");
        assertThat(contentType,equalTo("0"));

        //List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        //assertThat(AllHeaders.get(0).getValue(), equalTo("text/html"));
        logger.debug("CATEGORY SUCCESSFULLY DELETED");
    }





}
