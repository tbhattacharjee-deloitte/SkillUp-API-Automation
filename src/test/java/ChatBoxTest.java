import Auth.Auth;
import Base.BaseClass;
import Base.BaseProp;
import ListenersPackage.ListenerClass;
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
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Listeners(ListenerClass.class)
public class ChatBoxTest extends BaseClass {
    private String loginToken;
    private ChatBox chatBox;


    public ChatBoxTest() {
        super();
    }

    @BeforeClass
    @Parameters({"username", "password"})
    public void login(String username, String password) {
        loginToken = Auth.authToken;
        chatBox = new ChatBox(loginToken,logger);
        logger.debug("Login token is:" + loginToken);
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
    @Parameters ({"id"})
    public void addChatToChatbox(String id) {
        chatBox.addChatToChatbox(Integer.parseInt(id));
    }

    @Test (priority = 4)
    public void getAllChatOfChatbox() {
        chatBox.getAllChatOfChatbox();
    }


}
