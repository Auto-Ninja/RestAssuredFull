package smtauto;

import com.jayway.jsonpath.JsonPath;
import com.opencsv.exceptions.CsvValidationException;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.opencsv.CSVReader;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*@Epic("Epic-11")
@Feature("User Testing")*/
public class DataDrivenReportGeneration {

    /*@Story("Bulk Testing Story 1")
    @Description("end to end testing")
    @Severity(SeverityLevel.CRITICAL)*/
    @Test(dataProvider = "GetDataFromCSV")
    public void CSVFileReader(Map<String,String> testData) {

        System.out.println(testData.get("name") + " --- " + testData.get("email"));
    }

    @DataProvider(name = "GetDataFromCSV")
    public Object[][] GetDataFromCSV()
    {
        System.out.println("CSV Reader begin");
        String filepath = "./src/test/resources/payload/users.csv";
        Map<String,String> map = null;
        List<Map<String,String>> testDataList = null;
        Object[][] csvData=null;
        try {
            testDataList = new ArrayList<Map<String,String>>();
            CSVReader csvReader = new CSVReader(new FileReader(filepath));
            String[] row = null;
            int count = 0;
            while ((row = csvReader.readNext()) != null) {
                if (count == 0) {
                    count++;
                    continue;
                }

                map = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
                map.put("name", row[0]);
                map.put("email", row[1]);
                testDataList.add(map);

               /* for (int i = 0; i < row.length; i++) {
                    String column = row[i];
                    System.out.println(column);
                }*/
            }

            csvData = new Object[testDataList.size()][1];

            for (int i = 0; i < testDataList.size(); i++) {
                csvData[i][0] = testDataList.get(i);
            }

            /*for (int i = 0; i < csvData.length; i++) {
                System.out.println(csvData[i][0] );
            }*/

        } catch (
                FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (
                CsvValidationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (
                IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return csvData;
    }
}
    /*@Test(dataProvider = "usersdata")
    public void BulkTestAPI(Map<String,String> testUserData){
        String name = testUserData.get("FirstName");
        String email = testUserData.get("EmailAddress");
        JSONObject user = new JSONObject();
        user.put("name",name);
        user.put("email", email);
        PostApi("https://reqres.in/api/users",user.toJSONString());
    }*/
    /*private void PostApi(String url, String postAPIRequestBody){

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
                .statusCode(201)
                .extract()
                .response();

        System.out.println("Smi > "+response.body().asString());
        System.out.println("Smi > Done");

    }
*/

