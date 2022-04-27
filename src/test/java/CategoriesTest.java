import Auth.Auth;
import categories.Category;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import Auth.Auth;
import Base.BaseClass;

import static Base.BaseClass.extent;

public class CategoriesTest extends BaseClass{

    private String loginToken;
    private Category category;


    public CategoriesTest() {
        super();
        extent.attachReporter(new ExtentHtmlReporter("CategoriesTest.html"));
    }

    @BeforeClass
    public void login() {
        loginToken = Auth.login("vivek", "vivek123");
        category = new Category(loginToken, logger);
        logger.debug(loginToken);
    }

    @Test (priority = 1)
    public void createCategory() {
        category.createCategory("Testing");
    }

    @Test (priority = 2)
    public void getAllCategory() {
        category.getAllCategory();
    }

    @Test (priority = 3)
    public void getCategoryById() {
        category.getCategoryById(160);
    }

    @Test(priority = 4)
    public void getCategoryByName() {
        category.getCategoryByName("Testing");
    }

    @Test(priority = 5)
    public void updateCategory() {
        category.updateCategory(160,"BackEnd");
    }

    @Test (priority = 6)
    public void createSkill() {
        category.createSkill("CSS");
    }

    @Test(priority = 7)
    public void addSkillToCategory() {
        category.addSkillToCategory(160);
    }

    @Test(priority = 8)
    public void DeleteCategoryById() {
        category.DeleteCategoryById(160);
    }
}
