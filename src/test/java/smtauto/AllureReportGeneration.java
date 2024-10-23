package smtauto;
import java.io.File;
import java.io.File;
import java.io.IOException;

import com.jayway.jsonpath.JsonPath;
import constants.ApplicationConstants;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import com.testautomation.apitesting.listener.RestAssuredListener;
import net.minidev.json.JSONArray;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;


@Epic("Epic-01")
@Feature("Create Update Delete Booking")

public class AllureReportGeneration {
    private static final Logger logger = LogManager.getLogger(AllureReportGeneration.class);

    @Story("Story 1")
    @Test(description = "end to end api testing")
    @Description("end to end testing")
    @Severity(SeverityLevel.CRITICAL)
    public void e2eAPIRequest() {
        logger.info("e2eAPIRequest test execution started...");

        try {
            String url = "https://restful-booker.herokuapp.com/booking";
            String postAPIRequestBody = FileUtils.readFileToString(new File("./src/test/resources/payload/postapi.txt"),
                    "UTF-8");
            String tokenAPIRequestBody = FileUtils.readFileToString(new File(ApplicationConstants.TOKEN_API_REQUEST_BODY),
                    "UTF-8");
            String putAPIRequestBody = FileUtils.readFileToString(new File(ApplicationConstants.PUT_API_REQUEST_BODY),
                    "UTF-8");
            String patchAPIRequestBody = FileUtils.readFileToString(new File(ApplicationConstants.PATCH_API_REQUEST_BODY),
                    "UTF-8");
            int bookingId = PostApi(url, postAPIRequestBody);

            GetApi(url,bookingId);

            String token = TokenApi(ApplicationConstants.URL_TOKEN_API,tokenAPIRequestBody);

            PutApi(token,putAPIRequestBody,bookingId);
            PatchApi(token,patchAPIRequestBody,bookingId);
            //throw new IOException();

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("e2eAPIRequest test execution ended...");

    }
    private int PostApi(String url, String postAPIRequestBody){

        System.out.println("Payload = " + postAPIRequestBody);
        AllureRestAssured allureFilter = new AllureRestAssured();

        // post api call
        Response response = RestAssured
                .given().filter(allureFilter)
                //.filter(new RestAssuredListener())
                .contentType(ContentType.JSON).body(postAPIRequestBody)
                .baseUri(url)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("Smi > "+response.body().asString());
        System.out.println("Smi > Done");
        JSONArray jsonArray = JsonPath.read(response.body().asString(), "$.booking..firstname");
        String firstName = (String) jsonArray.get(0);

        Assert.assertEquals(firstName, "api testing");
        System.out.println("PostBody response" + response.body().asString());
        int bookingId = JsonPath.read(response.body().asString(), "$.bookingid");
        System.out.println("Booking Id : " + bookingId);
        return bookingId;
    }
    private void GetApi(String url, int bookingId){
        // get api call
        RestAssured
                .given().filter(new AllureRestAssured())
                //.filter(new RestAssuredListener())
                .contentType(ContentType.JSON)
                .baseUri(url)
                .when()
                .get("/{bookingId}", bookingId)
                .then()
                .assertThat()
                .statusCode(200);
        System.out.println("GetAPi executed");
    }
    private String  TokenApi(String url,String tokenAPIRequestBody){
        // token generation
        Response tokenAPIResponse = RestAssured
                .given().filter(new AllureRestAssured())
                //.filter(new RestAssuredListener())
                .contentType(ContentType.JSON)
                .body(tokenAPIRequestBody)
                .baseUri(url)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        String token = JsonPath.read(tokenAPIResponse.body().asString(), "$.token");
        System.out.println("Token Id : " + token);
        return token;
    }
    private void PutApi(String token,String putAPIRequestBody, int bookingId){
        // put api call
        Response response = RestAssured
                .given().filter(new AllureRestAssured())
                //.filter(new RestAssuredListener())
                .contentType(ContentType.JSON)
                .body(putAPIRequestBody)
                .header("Cookie", "token=" + token)
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                .put("/{bookingId}", bookingId)
                .then()
                .assertThat()
                .statusCode(200).body("firstname", equalTo("Specflow"))
                .body("lastname", equalTo("Selenium C#")).extract().response();
        System.out.println("Updated response : " + response.body().asString());

    }
    private void PatchApi(String token,String patchAPIRequestBody,int bookingId){
        // patch api call
        Response response = RestAssured
                .given().filter(new AllureRestAssured())
                //.filter(new RestAssuredListener())
                .contentType(ContentType.JSON)
                .body(patchAPIRequestBody)
                .header("Cookie", "token=" + token)
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .when()
                .patch("/{bookingId}", bookingId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstname", equalTo("Testers Talk")).extract().response();
        System.out.println("New response : " + response.body().asString());
    }

}
