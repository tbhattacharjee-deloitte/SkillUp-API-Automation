import Auth.Auth;
import Base.BaseClass;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
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
    public void loginUser(){
        loginToken = Auth.login("vivek", "vivek123");
        System.out.println("Login Token is "+loginToken);

        log.info("Token Is Generated");
    }
//CREATE NEW REQUEST
    @Test(priority = 1)
    public void create_new_request() throws IOException {
        log.info("Creating New Request");

        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        JsonPath create_data=new JsonPath(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))));

        given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                    .body(new String(Files.readAllBytes(Paths.get("src/test/resources/CreateNewRequest.json"))))

                .when().post("/api/requests/22/7")

                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                        .body("availStartTime",equalTo(create_data.getString("availStartTime"))).body("availEndTime",equalTo(create_data.getString("availEndTime")))
                        .body("description",equalTo(create_data.getString("description")));


    }

    //GET ALL REQUEST
    @Test(priority = 2)
    public void get_all_request(){
        log.info("Getting all requests");

        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";

        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/")
                .then().log().all().assertThat().statusCode(200);

    }

    //GET REQUEST BY ID
    @Test(priority = 3)
    public void get_request_by_id(){
        log.info("getting request by id");

        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        int id=87;

        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/"+id)
                .then().log().all().assertThat().statusCode(200).body("requestId",equalTo(id));

    }
    //GET USER OF THIS REQUEST
    @Test(priority = 4)
    public void get_userrequest(){
        log.info("Getting user of this request");
        int req_id=87;

        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";

        String responsebody=given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/getUser/"+req_id)

                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js=new JsonPath(responsebody);
        int count_total_request= js.getInt("requests.size()");

        System.out.println("total requests are "+count_total_request);

    }

    //UPDATE REQUEST DETAILS
    @Test(priority = 5)
    public void update_request() throws IOException {
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        int update_requestID=87;

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
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";

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



