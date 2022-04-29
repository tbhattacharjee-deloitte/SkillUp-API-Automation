package Skills;

import Base.BaseClass;
import Base.BaseProp;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
import static io.restassured.RestAssured.given;


public class Skills {
    private final String token;
    private final Logger logger;
    private final ResponseSpecification resSpec;
    private final RequestSpecification reqSpec;
    private int createdSkillId;

    public Skills(String token, Logger logger) {
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

    public void createSkill(String skillName) {
        JSONObject obj = new JSONObject();
        obj.put("skillName", skillName);
        BaseClass.test.log(Status.INFO, "Creating new skill with name = " + skillName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/skills/").then().spec(resSpec).extract().response();
        BaseClass.test.log(Status.INFO, "Response code = " + response.statusCode());
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        assert res.has("skillId") && res.has("skillName");
        logger.debug(res.toString());
        this.createdSkillId = Integer.parseInt(res.get("skillId").toString());
        BaseClass.test.log(Status.INFO, "new skill id = " + this.createdSkillId);
    }

    public void deleteLastCreatedSkill() {
        Response response = given().spec(reqSpec)
                .delete("/api/skills/" + this.createdSkillId).then()
                .extract().response();
        assert response.statusCode() == BaseProp.OK;
    }

    public void getAllSkills() {
        BaseClass.test.log(Status.INFO, "Getting all skills");
        Response response = given().spec(reqSpec).get("api/skills/").then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONArray arr = new JSONArray(response.asString());
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            assert obj.has("skillId") && obj.has("skillName");
        }
        logger.debug(arr);
    }

    public void getSkillByID(int id) {
        Response response = given().spec(reqSpec).get("api/skills/" + id).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        BaseClass.test.log(Status.INFO, "Getting skill by id\n" + res.toString());
        assert res.has("skillId") && res.has("skillName");
        logger.debug("Skill data with id = {" + id + "} => " + res);
    }

    public void getSkillByName(String name) {
        Response response = given().spec(reqSpec).param("skillName", name).
                get("/api/skills/name").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        BaseClass.test.log(Status.INFO, "Getting skill by name\n" + res.toString());
        assert res.has("skillId") && res.has("skillName");
        logger.debug(res);
    }

    public void updateSkills(int id, String skillName) {
        BaseClass.test.log(Status.INFO, "Updating skills");
        getSkillByID(id);
        JSONObject obj = new JSONObject();
        obj.put("skillName", skillName);
        Response response = given().spec(reqSpec).body(obj.toString())
                .put("/api/skills/update/" + id).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        BaseClass.test.log(Status.INFO, "Updated skill = \n" + res.toString());
        assert res.has("skillId") && res.has("skillName");
        logger.debug("Updated skill => " + res);
    }
}
