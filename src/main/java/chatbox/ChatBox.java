package chatbox;

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

public class ChatBox {
    private final String token;
    private final ResponseSpecification resSpec;
    private final RequestSpecification reqSpec;
    int createdChatboxId;


    public ChatBox(String token) {
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

    //creating a chat box
    public void createChatBox() {
        JSONObject obj = new JSONObject();
        Response response = given().spec(reqSpec).body(obj)
                .post("/api/chatbox/").then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONObject res = new JSONObject(response.asString());
        System.out.println("Chat box created is:" + res);
        this.createdChatboxId = Integer.parseInt(res.get("chatboxId").toString());
    }

    //getting all the chat box
    public void getAllChatBox() {
        Response response = given().spec(reqSpec).get("/api/chatbox/").then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONArray arr = new JSONArray(response.asString());
        System.out.println("getting all chatbox:" + arr);
    }

    //adding a chat to chatbox
    public void addChatToChatbox(int chatId) {
        Response response = given().spec(reqSpec)
                .put("/api/chatbox/addChat/"+createdChatboxId+"/"+chatId).then().spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        System.out.println("chat added to chatbox is " + response.asString());
    }


    //getting all the chat of the given chatbox
    public void getAllChatOfChatbox() {
        Response response = given().spec(reqSpec).get("/api/chatbox/get-chats/"+createdChatboxId).then()
                .spec(resSpec).extract().response();
        assert response.statusCode() == BaseProp.OK;
        JSONArray arr = new JSONArray(response.asString());
        System.out.println("chat of given chatbox id is:" + arr);
    }
}
