import Auth.Auth;
import Base.BaseClass;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.*;
@Listeners(DemoListener.class)
public class RequestTest extends BaseClass {
    static Logger log = Logger.getLogger(String.valueOf(RequestTest.class));
    private String loginToken;

    public RequestTest(){
        super();
        extent.attachReporter(new ExtentHtmlReporter("Demo.html"));
    }

    @BeforeClass
    @Parameters({"username", "password"})
    public void loginUser(String username, String password){
        loginToken = Auth.authToken;
        System.out.println("Login Token is "+loginToken);

        log.info("Token Is Generated");
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";

    }
//CREATE NEW REQUEST
    @Test(priority = 1)
    public void create_new_request() throws IOException {
        log.info("Creating New Request");

        JsonPath create_data=new JsonPath(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))));

        given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                    .body(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))))

                .when().post("/api/requests/22/7")

                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                        .body("availStartTime",equalTo(create_data.getString("availStartTime"))).body("availEndTime",equalTo(create_data.getString("availEndTime")))
                        .body("description",equalTo(create_data.getString("description")));

        System.out.println("Start Time is "+create_data.getString("availStartTime")+" End Time is "+create_data.getString("availEndTime"));
        System.out.println("Description is =>"+create_data.getString("description"));
    }

    //GET ALL REQUEST
    @Test(priority = 2)
    public void get_all_request(){
        log.info("Getting all requests");


        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/")
                .then().log().all().assertThat().statusCode(200).contentType("application/json");

    }

    //GET REQUEST BY ID
    @Test(priority = 3)
    public void get_request_by_id() throws IOException {
        log.info("getting request by id");
        JsonPath create_data=new JsonPath(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))));

        String postresponse=given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))))

                .when().post("/api/requests/22/7")

                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                .body("availStartTime",equalTo(create_data.getString("availStartTime"))).body("availEndTime",equalTo(create_data.getString("availEndTime")))
                .body("description",equalTo(create_data.getString("description"))).extract().response().asString();

        JsonPath id=new JsonPath(postresponse);
        int req_id=id.getInt("requestId");

        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/"+req_id)
                .then().log().all().assertThat().statusCode(200).body("requestId",equalTo(req_id));

    }
    //GET USER OF THIS REQUEST
    @Test(priority = 4)
    public void get_userrequest() throws IOException {
        log.info("Getting user of this request");
        JsonPath create_data=new JsonPath(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))));

        String postresponse=given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))))

                .when().post("/api/requests/22/7")

                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                .body("availStartTime",equalTo(create_data.getString("availStartTime"))).body("availEndTime",equalTo(create_data.getString("availEndTime")))
                .body("description",equalTo(create_data.getString("description"))).extract().response().asString();

        JsonPath id=new JsonPath(postresponse);
        int req_id=id.getInt("requestId");



        String responsebody=given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/getUser/"+req_id)

                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath size=new JsonPath(responsebody);
        int count_total_request= size.getInt("requests.size()");

        System.out.println("total requests are "+count_total_request);

    }

    //UPDATE REQUEST DETAILS
    @Test(priority = 5)
    public void update_request() throws IOException {
        JsonPath create_data=new JsonPath(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))));

        String postresponse=given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))))

                .when().post("/api/requests/22/7")

                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                .body("availStartTime",equalTo(create_data.getString("availStartTime"))).body("availEndTime",equalTo(create_data.getString("availEndTime")))
                .body("description",equalTo(create_data.getString("description"))).extract().response().asString();

        JsonPath id=new JsonPath(postresponse);
        int update_requestID=id.getInt("requestId");

        JsonPath update_data=new JsonPath(new String(Files.readAllBytes(Paths.get("src/test/resources/Updatedata.json"))));

        String updateresponse=given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/Updatedata.json")))).

                when().put("/api/requests/update/"+update_requestID)

                .then().log().all().assertThat().statusCode(200).contentType("application/json")

                .body("duration",equalTo(update_data.getInt("duration")))
                .body("availStartTime",equalTo(update_data.getString("availStartTime")))
                .body("availEndTime",equalTo(update_data.getString("availEndTime"))).extract().response().asString();

        //fetching data from response
        JsonPath js=new JsonPath(updateresponse);
        int duration_value= js.getInt("duration");
        int reqID=js.getInt("requestId");

        //Verifying update call by get call
        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/"+reqID)
                .then().log().all().assertThat().statusCode(200).body("requestId",equalTo(reqID))
                .body("duration",equalTo(duration_value));

        log.info("Updated request Details");
    }

    //DELETE REQUEST BY ID
    @Test(priority = 6)
    public void delete_requestby_id() throws IOException {

        String postbody=given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                .body(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))))

                .when().post("/api/requests/22/7")

                .then().log().all().assertThat().statusCode(200).contentType("application/json").extract().response().asString();

        JsonPath postdata=new JsonPath(postbody);
        int requestID= postdata.getInt("requestId");

        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().delete("/api/requests/"+requestID).then().log().all().assertThat().statusCode(200);

        log.info("Deleted Request by id");

        //Verifying by get method
        //getting deleted request by id
        //status = 404 not found because its deleted

        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/"+requestID)
                .then().log().all().assertThat().statusCode(404);

        log.info("Request ID Was Deleted Successfully");
    }
    }



