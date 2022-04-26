package Skills;

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
        Response response = given().spec(reqSpec).body(obj.toString())
                .post("/api/skills/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        logger.debug(res.toString());
        this.createdSkillId = Integer.parseInt(res.get("skillId").toString());
    }

    public void deleteLastCreatedSkill() {
        Response response = given().spec(reqSpec)
                .delete("/api/skills/" + this.createdSkillId).then()
                .extract().response();
        assert response.statusCode() == BaseProp.OK;
    }
}
