package TestClassPackage;

import Auth.Auth;
import Base.BaseClass;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SkillsApiTest extends BaseClass {
    private String loginToken;

    public SkillsApiTest() {
        super();
        extent.attachReporter(new ExtentHtmlReporter("SkillsApiTest.html"));
    }

    @BeforeClass
    public void login() {
        loginToken = Auth.login("vivek", "vivek123");
        logger.debug(loginToken);
    }

    @Test (priority = 1)
    public void createSkill() {

    }
}
