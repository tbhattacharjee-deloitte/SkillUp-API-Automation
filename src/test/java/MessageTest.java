import Auth.Auth;
import Base.BaseClass;
import Base.BaseProp;
import ListenersPackage.ListenerClass;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Listeners(ListenerClass.class)
public class MessageTest {
    RequestSpecification requestSpecification;
    private String LoginToken;
    String BaseUri = BaseProp.baseUrl+"/api/messages";
    @BeforeTest
    @Parameters({"username", "password"})
    public void login(String username, String password){
        LoginToken = Auth.login(username, password);
//        System.out.println(LoginToken);
    }
    @Test
    public void GetAllMessages(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get("/");
        ResponseBody responseBody = response.getBody();
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 1)
    public void GetMessageByID(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        String MessageID = "26";
        Response response = requestSpecification.get("/"+MessageID);
        ResponseBody responseBody = response.getBody();
        JsonPath jsonPath = new JsonPath(responseBody.asString());
        int messageId = jsonPath.getInt("messageId");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        assertThat(messageId, equalTo(Integer.parseInt(MessageID)));
    }
    @Test(priority = 2)
    public void CreateMessageByRefID(){
        File jsonData = new File("src/test/resources/CreateMessageByRefID.json");
        requestSpecification = with().
                baseUri(BaseUri).
                body(jsonData).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.put("/26");
        ResponseBody responseBody = response.getBody();
        List<Header> ContentType = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(ContentType.get(0).getValue(), equalTo("text/html"));

    }
    @Test(priority = 3)
    public void DeleteMessageByID(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.delete("/30");
        List<Header> ContentType= response.getHeaders().getList("Content-Type");
        List<Header> ContentLength = response.getHeaders().getList("Content-Length");
        assertThat(ContentType.get(0).getValue(), equalTo("application/json"));
        assertThat(ContentLength.get(0).getValue(), equalTo(0));
        assertThat(response.statusCode(), equalTo(200));
    }

    // negative test cases
    // invalid Login token
    @Test(priority = 4)
    public void GetAllMessagesNegativeTest(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken+"k");
        Response response = requestSpecification.get("/");
        ResponseBody responseBody = response.getBody();
        JsonPath jsonPath = new JsonPath(responseBody.asString());
        String Error = jsonPath.getString("error");
        String Message = jsonPath.getString("message");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(BaseProp.UNAUTH));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        assertThat(Error, equalTo("Unauthorized"));
        assertThat(Message, equalTo("Unauthorized : Server"));
    }

    // deleting non-existent message
    @Test(priority = 5)
    public void DeleteMessageByIDNegativeTest(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);

        Response response = requestSpecification.delete("/30");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        ResponseBody responseBody = response.getBody();
        JsonPath jsonPath = new JsonPath(responseBody.asString());
        assertThat(response.statusCode(), equalTo(404));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        assertThat(jsonPath.getInt("status"), equalTo(404));
    }
}
