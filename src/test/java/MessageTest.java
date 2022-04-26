import Auth.Auth;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MessageTest {
    RequestSpecification requestSpecification;
    private String LoginToken;
    String BaseURI = "https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app/api/messages";
    @BeforeTest
    public void login(){
        LoginToken = Auth.login("vivek", "vivek123");
//        System.out.println(LoginToken);
    }
    @Test
    public void GetAllMessages(){
        requestSpecification = with().
                baseUri(BaseURI).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get("/");
        ResponseBody responseBody = response.getBody();
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(200));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 1)
    public void GetMessageByID(){
        requestSpecification = with().
                baseUri(BaseURI).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get("/26");
        ResponseBody responseBody = response.getBody();
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(200));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 3)
    public void CreateMessageByRefID(){

    }
    @Test(priority = 4)
    public void DeleteMessageByID(){

    }
}
