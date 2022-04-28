package chatbox;

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

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ChatBox {
    private final String token;
    private final Logger logger;
    private final ResponseSpecification resSpec;
    private final RequestSpecification reqSpec;
    int createdChatboxId;


    public ChatBox(String token,Logger logger) {
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

    //creating a chat box
    public void createChatBox() {
        JSONObject obj = new JSONObject();
        Response response = given().spec(reqSpec).body(obj)
                .post("/api/chatbox/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        logger.debug(res);
        this.createdChatboxId = Integer.parseInt(res.get("chatboxId").toString());
    }

    //getting all the chat box
    public void getAllChatBox() {
        Response response = given().spec(reqSpec).get("/api/chatbox/").then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        JSONArray arr = new JSONArray(response.asString());
        logger.debug(arr);
    }

    //adding a chat to chatbox
    public void addChatToChatbox(int chatId) {
        Response response = given().spec(reqSpec)
                .put("/api/chatbox/addChat/"+createdChatboxId+"/"+chatId).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        logger.debug(response.asString());
    }


    //getting all the chat of the given chatbox
    public void getAllChatOfChatbox() {
        Response response = given().spec(reqSpec).get("/api/chatbox/get-chats/"+createdChatboxId).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        JSONArray arr = new JSONArray(response.asString());
        logger.debug("chat of given chatbox id is:" + arr);
    }
}
