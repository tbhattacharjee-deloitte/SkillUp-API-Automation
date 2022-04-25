import Auth.Auth;
import Base.BaseClass;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(DemoListener.class)
public class DemoTest extends BaseClass {
    private String loginToken;

    public DemoTest() {
        super();
        extent.attachReporter(new ExtentHtmlReporter("Demo.html"));
    }

    @BeforeClass
    public void login() {
        loginToken = Auth.login("vivek", "vivek123");
        logger.debug(loginToken);
    }

    @Test
    public void test() {

    }
}
