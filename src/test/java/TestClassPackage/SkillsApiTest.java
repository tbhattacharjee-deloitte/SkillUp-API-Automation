package TestClassPackage;

import Auth.Auth;
import Base.BaseClass;
import Skills.Skills;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SkillsApiTest extends BaseClass {
    private String loginToken;
    private Skills skills;

    public SkillsApiTest() {
        super();
        extent.attachReporter(new ExtentHtmlReporter("SkillsApiTest.html"));
    }

    @BeforeClass
    public void login() {
        loginToken = Auth.login("vivek", "vivek123");
        skills = new Skills(loginToken, logger);
        logger.debug(loginToken);
    }

    @Test (priority = 1)
    public void createSkill() {
        skills.createSkill("AI");
    }

    @Test (priority = 2)
    public void deleteLastSkill() {
        skills.deleteLastCreatedSkill();
    }
}
