import Auth.Auth;
import Base.BaseProp;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Listeners(DemoListener.class)
public class TrainingDetailsTest {
    RequestSpecification requestSpecification;
    private String LoginToken;
    String BaseUri = BaseProp.baseUrl+"/api/trainings";
    String Status;
    public int req_id;
    @BeforeTest
    @Parameters({"username", "password"})
    public void login(String username, String password){
        LoginToken = Auth.login(username, password);
//        System.out.println(LoginToken);
    }

    @Test(priority = 1)
    public void  createtrainingId() throws IOException {

        JsonPath create_data=new JsonPath(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))));

        String createId =given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + LoginToken)
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))))

                .when().post("/api/requests/22/7")

                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                .body("availStartTime",equalTo(create_data.getString("availStartTime"))).body("availEndTime",equalTo(create_data.getString("availEndTime")))
                .body("description",equalTo(create_data.getString("description"))).extract().response().asString();

        JsonPath id=new JsonPath(createId);
        req_id=id.getInt("requestId");

    }


    @Test(priority = 2)
    public void CreateTraining(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        //String RequestID = "218";
        String UserID = "25";
        Response response = requestSpecification.post("/"+req_id+"/"+UserID);
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        JSONObject res = new JSONObject(response.asString());
        System.out.println(res);
    }
    @Test(priority = 3)
    public void GetAllTrainings(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get( "/");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 4)
    public void GetTrainingsByID(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get("/4");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        ResponseBody responseBody = response.getBody();
        JsonPath jsonPath = new JsonPath(response.asString());
        int TrainingID = jsonPath.getInt("trainingId");
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(TrainingID, equalTo(4));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 5)
    public void UpdateTrainingStatus(){
        requestSpecification = with().
                baseUri(BaseUri).body("{}").
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.patch("/updateStatus/9");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 6)
    public void DeleteTraining(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.delete("/trainings/"+req_id);
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        ResponseBody responseBody = response.getBody();
        int k = responseBody.toString().length();
        assertThat(response.statusCode(), equalTo(BaseProp.OK));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
        assertThat(k, equalTo(0));
    }
}
