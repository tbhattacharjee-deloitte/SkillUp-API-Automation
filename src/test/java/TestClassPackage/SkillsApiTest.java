package TestClassPackage;

import Auth.Auth;
import Base.BaseClass;
import ListenersPackage.ListenerClass;
import Skills.Skills;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.UUID;

@Listeners(ListenerClass.class)
public class SkillsApiTest extends BaseClass {
    private String loginToken;
    private Skills skills;

    public SkillsApiTest() {
        super();
    }

    @BeforeClass
    @Parameters ({"username", "password"})
    public void login(String username, String password) {
        loginToken = Auth.login(username, password);
        skills = new Skills(loginToken, logger);
        logger.debug(loginToken);
    }

    @Test (priority = 1)
    @Parameters ({"createNewSkill"})
    public void createSkill(String createNewSkill) {
        skills.createSkill(createNewSkill);
    }

    @Test (priority = 2)
    public void deleteLastSkill() {
        skills.deleteLastCreatedSkill();
    }

    @Test (priority = 3)
    public void getAllSkills() {
        skills.getAllSkills();
    }

    @Test (priority = 4)
    @Parameters({"skillID"})
    public void getSkillById(String skillID) {
        skills.getSkillByID(Integer.parseInt(skillID));
    }

    @Test (priority = 5)
    @Parameters ({"existingSkillName"})
    public void getSkillByName(String existingSkillName) {
        skills.getSkillByName(existingSkillName);
    }

    @Test (priority = 6)
    @Parameters ({"updSkillId"})
    public void updateSkill(String updSkillId) {
        skills.updateSkills(Integer.parseInt(updSkillId), UUID.randomUUID().toString());
    }
}
