import Auth.Auth;
import Base.BaseClass;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
@Listeners(DemoListener.class)
public class RequestTest extends BaseClass {
    private String loginToken;

    public RequestTest(){
        super();
        extent.attachReporter(new ExtentHtmlReporter("Demo.html"));
    }

    @BeforeClass
    public void loginUser(){
        loginToken = Auth.login("vivek", "vivek123");
        System.out.println("Login Token is "+loginToken);
    }
//CREATE NEW REQUEST
    @Test
    public void create_new_request(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
                given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                    .body("{\n" +
                            "    \"startDate\": \"12-05-2022\",\n" +
                            "    \"availStartTime\": \"10:12:30\",\n" +
                            "    \"availEndTime\": \"10:12:45\",\n" +
                            "    \"description\": \"To master this skill.\",\n" +
                            "    \"duration\": 3\n" +
                            "}")
                .when().post("/api/requests/22/7")
                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                        .body("availStartTime",equalTo("10:12:30")).body("availEndTime",equalTo("10:12:45"))
                        .body("description",equalTo("To master this skill."));

    }

    //GET ALL REQUEST
    @Test
    public void get_all_request(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/")
                .then().log().all().assertThat().statusCode(200);
    }

    //GET REQUEST BY ID
    @Test
    public void get_request_by_id(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/87")
                .then().log().all().assertThat().statusCode(200).body("requestId",equalTo(87));
    }
    //GET USER OF THIS REQUEST
    @Test
    public void get_userrequest(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        String responsebody=given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/getUser/87")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
        JsonPath js=new JsonPath(responsebody);
        int count_total_request= js.getInt("requests.size()");
        System.out.println("total requests are "+count_total_request);
    }
    //UPDATE REQUEST DETAILS
    @Test
    public void update_request(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        String updateresponse=given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                .body("{\n" +
                        "    \"startDate\": \"24-07-2030\",\n" +
                        "    \"availStartTime\": \"10:08:28\",\n" +
                        "    \"availEndTime\": \"10:09:43\",\n" +
                        "    \"duration\": 3\n" +
                        "}").
                when().put("/api/requests/update/87")
                .then().log().all().assertThat().statusCode(200).contentType("application/json")
                .body("duration",equalTo(3)).body("availStartTime",equalTo("10:08:28"))
                .body("availEndTime",equalTo("10:09:43")).extract().response().asString();
        JsonPath js=new JsonPath(updateresponse);
        int duration_value= js.getInt("duration");

        //Verifying update call by get call
        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().get("/api/requests/87")
                .then().log().all().assertThat().statusCode(200).body("requestId",equalTo(87))
                .body("duration",equalTo(duration_value));

    }
    //DELETE REQUEST BY ID
    @Test
    public void delete_requestby_id(){
        RestAssured.baseURI="https://hu-monitorapp-backend-urtjok3rza-wl.a.run.app";
        String postbody=given().log().all().header("Content-Type", "application/json").header("Authorization", "Bearer " + loginToken)
                .body("{\n" +
                        "    \"startDate\": \"12-05-2022\",\n" +
                        "    \"availStartTime\": \"10:12:30\",\n" +
                        "    \"availEndTime\": \"10:12:45\",\n" +
                        "    \"description\": \"To master this skill.\",\n" +
                        "    \"duration\": 3\n" +
                        "}")
                .when().post("/api/requests/22/7")
                .then().log().all().assertThat().statusCode(200).contentType("application/json").extract().response().asString();

        JsonPath js=new JsonPath(postbody);
        int requestID= js.getInt("requestId");
        given().log().all().header("Authorization", "Bearer " + loginToken)
                .when().delete("/api/requests/"+requestID).then().log().all().assertThat().statusCode(200);
    }

    }



