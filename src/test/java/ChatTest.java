import Auth.Auth;
import Base.BaseClass;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;

public class ChatTest extends BaseClass {
    static Logger log = Logger.getLogger(String.valueOf(RequestTest.class));
    private String loginToken;

    public ChatTest(){
        super();
        extent.attachReporter(new ExtentHtmlReporter("Demo.html"));
    }

    @BeforeClass
    public void loginUser(){
        loginToken = Auth.login("vivek", "vivek123");
        System.out.println("Login Token is "+loginToken);

        log.info("Token Is Generated");
    }
    @Test(priority = 1)
    public void get_all_chats(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/chats/")
                .then().log().all().assertThat().statusCode(200).contentType("application/json");

    }
    @Test(priority = 2)
    public void get_chat_by_id(){
        int chatid=1;
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
       String getresponse= given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/chats/"+chatid)
                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                .body("chatId",equalTo(chatid)).extract().response().asString();
        JsonPath responsebody=new JsonPath(getresponse);
        System.out.println("Chat : "+responsebody.getString("chat"));
        System.out.println("SenderName is: "+responsebody.getString("senderName"));
    }
    @Test(priority = 3)
    public void chat_by_chatboxID() throws IOException {
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        given().log().all().queryParam("senderName","Bhavesh").header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + loginToken)
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/chat.json"))))
                .with().put("/api/chats/10")
                .then().log().all().assertThat().statusCode(200);
    }
    @Test(priority = 4)
    public void delete_chatByID(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        given().log().all().header("Authorization", "Bearer " + loginToken)
                .with().put("/api/chats/3")
                .then().log().all().assertThat().statusCode(200);
    }
}
