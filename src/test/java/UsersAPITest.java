
import Auth.Auth;
import Base.BaseClass;

import Users.Users;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.lang.ref.PhantomReference;
import java.util.Properties;
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
        Users.get_user_by_ID(Users.addeduserId);
    }

    @Test(priority = 4)
    public void update_user(){
        Users.update_user(Integer.parseInt(users.prop.getProperty("id1")), users.prop.getProperty("field1"), users.prop.getProperty("value1"));
    }

    @Test(priority = 5)
    public void delete_user_by_id(){
        Users.delete_user();
    }

    @Test(priority = 6)
    public void get_all_training_as_trainee(){
        Users.get_all_trainings_as_trainee(Integer.parseInt(users.prop.getProperty("id2")));
    }

    @Test(priority = 7)
    public void get_all_training_as_trainer(){
        Users.get_all_trainings_as_trainer(Integer.parseInt(users.prop.getProperty("id2")));
    }

}
