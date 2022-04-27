import Auth.Auth;
import Base.BaseProp;
import com.google.gson.JsonObject;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ReferenceTest {
    RequestSpecification requestSpecification;
    private String LoginToken;
    String BaseURI = BaseProp.baseUrl +"/api/references";
    @BeforeTest
    public void login(){
        LoginToken = Auth.login("vivek", "vivek123");
//        System.out.println(LoginToken);
    }

    @Test
    public void CreateReference(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageId", 35);
        jsonObject.put("message", "some_random_link");
        jsonObject.put("context", "some_random_context");
        jsonObject.put("dateStamp", "05-15-2022");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        JSONObject RequestBody = new JSONObject();
        RequestBody.put("messages", jsonArray);
        requestSpecification = with().
                baseUri(BaseURI).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.post("/");
        ResponseBody responseBody = response.getBody();
        JsonPath jsonPath = new JsonPath(responseBody.asString());
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
    }
    @Test(priority = 1)
    public void GetAllReferences(){
        requestSpecification = with().
                baseUri(BaseURI).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get("/");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(200));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 2)
    public void GetAllMessagesOfReference(){
        requestSpecification = with().
                baseUri(BaseURI).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get("/get-messages/19");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(200));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
}
