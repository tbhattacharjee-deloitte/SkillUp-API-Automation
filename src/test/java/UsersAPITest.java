
import Auth.Auth;
import Base.BaseClass;

import Users.Users;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
    public void get_all_user_test(){
        Users.get_all_users();
    }

    @Test(priority = 2)
    public void get_user_by_id(){
        Users.get_user_by_ID(1);
    }

    @Test(priority = 3)
    public void get_all_training_as_trainee(){
        Users.get_all_trainings_as_trainee(1);
    }

    @Test(priority = 4)
    public void get_all_training_as_trainer(){
        Users.get_all_trainings_as_trainer(2);
    }

}
