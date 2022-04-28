import Auth.Auth;
import ListenersPackage.ListenerClass;
import categories.Category;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Base.BaseClass;

@Listeners(ListenerClass.class)
public class CategoriesTest extends BaseClass{

    private String loginToken;
    private Category category;


    public CategoriesTest() {
        super();
    }

    @BeforeClass
    @Parameters({"username", "password"})
    public void login(String username, String password) {
//        String username = prop.getProperty("username");
//        String password = prop.getProperty("password");
        loginToken = Auth.login(username, password);
        category = new Category(loginToken);
        System.out.println("Login token is: " + loginToken);
    }

    @Test (priority = 1)
    public void createCategory() {
        String CategoryName = prop.getProperty("createCategoryName");
        category.createCategory(CategoryName);
    }

    @Test (priority = 2)
    public void getAllCategory() {
        category.getAllCategory();
    }

    @Test (priority = 3)
    public void getCategoryById() {
        category.getCategoryById(category.createdCategoryId);
    }

    @Test(priority = 4)
    public void getCategoryByName() {
        String CategoryName = prop.getProperty("getCategoryName");
        category.getCategoryByName(CategoryName);
    }

    @Test(priority = 5)
    public void updateCategory() {
        String CategoryName = prop.getProperty("updateCategoryName");
        category.updateCategory(category.createdCategoryId,CategoryName);
    }

    @Test (priority = 6)
    public void createSkill() {
        String CategoryName = prop.getProperty("createSkillName");
        category.createSkill(CategoryName);
    }

    @Test(priority = 7)
    public void addSkillToCategory() {
        category.addSkillToCategory(category.createdCategoryId);
    }

    @Test(priority = 8)
    public void DeleteCategoryById() {
        category.DeleteCategoryById(category.createdCategoryId);
    }
}
