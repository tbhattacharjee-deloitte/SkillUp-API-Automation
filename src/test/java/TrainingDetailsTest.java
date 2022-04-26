import Auth.Auth;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TrainingDetailsTest {
    RequestSpecification requestSpecification;
    private String LoginToken;
    String BaseUri = "https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app/api/trainings";
    @BeforeTest
    public void login(){
        LoginToken = Auth.login("vivek", "vivek123");
//        System.out.println(LoginToken);
    }
    @Test
    public void CreateTraining(){
        System.out.println("pass");
    }
    @Test(priority = 1)
    public void GetAllTrainings(){
         requestSpecification = with().
                 baseUri(BaseUri).
                 header("Content-Type", "application/json").
                 header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.get("/");
        List<Header> AllHeaders = response.getHeaders().getList("Content-Type");
        assertThat(response.statusCode(), equalTo(200));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test(priority = 2)
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
        assertThat(response.statusCode(), equalTo(200));
        assertThat(TrainingID, equalTo(4));
        assertThat(AllHeaders.get(0).getValue(), equalTo("application/json"));
    }
    @Test
    public void UpdateTrainingStatus(){
        requestSpecification = with().
                baseUri(BaseUri).
                header("Content-Type", "application/json").
                header("Authorization", "Bearer " + LoginToken);
        Response response = requestSpecification.patch("/updateStatus/9");
        
    }
    @Test
    public void DeleteTraining(){

    }
}
