import Auth.Auth;
import Base.BaseProp;
import chatbox.ChatBox;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ChatBoxTest {
    private String loginToken;
    private ChatBox chatBox;


    public ChatBoxTest() {
        super();
    }

    @BeforeClass
    public void login() {
        loginToken = Auth.login("vivek", "vivek123");
        chatBox = new ChatBox(loginToken);
        System.out.println("Login token is:" + loginToken);
    }

    @Test(priority = 1)
    public void createChatBox() {
        chatBox.createChatBox();
    }

    @Test (priority = 2)
    public void getAllChatBox() {
        chatBox.getAllChatBox();
    }


    @Test(priority = 3)
    public void addChatToChatbox() {
        chatBox.addChatToChatbox(24);
    }

    @Test (priority = 4)
    public void getAllChatOfChatbox() {
        chatBox.getAllChatOfChatbox();
    }


}
