
import Auth.Auth;
import Base.BaseClass;

import Users.Users;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.ref.PhantomReference;
import java.util.UUID;

public class UsersAPITest extends BaseClass{
    private String loginToken;
    private Users users;

    @BeforeClass
    public void login() {
        loginToken = Auth.login("vivek", "vivek123");
        users = new Users(loginToken, logger);
//        logger.debug(loginToken);
    }

    @Test(priority = 1)
    public void create_user(){
        Users.createUser();
    }

    @Test(priority = 2)
    public void get_all_user_test(){
        Users.get_all_users();
    }

    @Test(priority = 3)
    public void get_user_by_id(){
        Users.get_user_by_ID(1);
    }

    @Test(priority = 4)
    public void update_user(){
        Users.update_user(1,40);
    }

    @Test(priority = 5)
    public void delete_user_by_id(){
        Users.delete_user();
    }

    @Test(priority = 6)
    public void get_all_training_as_trainee(){
        Users.get_all_trainings_as_trainee(1);
    }

    @Test(priority = 7)
    public void get_all_training_as_trainer(){
        Users.get_all_trainings_as_trainer(2);
    }

}

//public class SkillsApiTest extends BaseClass {
//    private String loginToken;
//    private Skills skills;
//
//    public SkillsApiTest() {
//        super();
//        extent.attachReporter(new ExtentHtmlReporter("SkillsApiTest.html"));
//    }
//
//    @BeforeClass
//    public void login() {
//        loginToken = Auth.login("vivek", "vivek123");
//        skills = new Skills(loginToken, logger);
//        logger.debug(loginToken);
//    }
//
//    @Test (priority = 1)
//    public void createSkill() {
//        skills.createSkill("AI");
//    }
//
//    @Test (priority = 2)
//    public void deleteLastSkill() {
//        skills.deleteLastCreatedSkill();
//    }
//
//    @Test (priority = 3)
//    public void getAllSkills() {
//        skills.getAllSkills();
//    }
//
//    @Test (priority = 4)
//    public void getSkillById() {
//        skills.getSkillByID(5);
//    }
//
//    @Test (priority = 5)
//    public void getSkillByName() {
//        skills.getSkillByName("Java");
//    }
//
//    @Test (priority = 6)
//    public void updateSkill() {
//        skills.updateSkills(29, UUID.randomUUID().toString());
//    }
