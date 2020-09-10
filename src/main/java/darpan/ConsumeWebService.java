package darpan;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServlet;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

/**
 *
 * @author DarpanTeam
 */
public class ConsumeWebService extends HttpServlet implements PMDarpanCode {

    PMDarpanUtility du = new PMDarpanUtility();

    final String GetDateRangeAPIUrl = "http://prayasapi.darpan.nic.in/getdate";

    final String PushDataAPIUrl = "http://prayasapi.darpan.nic.in/pushdata";

    Date date = new Date();

    public String consumeREST(String path) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        String request_start = "", request_end = "", returnresp = "";
        String RetDMDashCaption = "";
        Timestamp start;
        Timestamp end;
        int statusCode = 0;

        List<Date> dateranges = new ArrayList<Date>();

        try {
            /*Create POST request for http://prayasapi.darpan.nic.in/getdate */
            HttpPost postRequest = new HttpPost(GetDateRangeAPIUrl);
            postRequest.addHeader("content-type", "application/json");
            /*Set Input Parameters*/
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("Instance_Code", instanceCode);
            jsonObj.put("Sec_Code", secCode);
            jsonObj.put("Ministry_Code", ministryCode);
            jsonObj.put("Dept_Code", deptCode);
            jsonObj.put("Project_Code", projectCode);

            StringEntity userEntity = new StringEntity(jsonObj.toString());
            postRequest.setEntity(userEntity);

            /*Send the request*/
            start = du.getCurrentTimeStamp();
            HttpResponse response = httpClient.execute(postRequest);
            end = du.getCurrentTimeStamp();

            /*Checking Status Code*/
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201) {
                du.pushLog(instanceCode, secCode, ministryCode, deptCode, projectCode, groupId, start, end, "Request Failed with HTTP error code", date, date, 0, statusCode);
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }

            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
            JSONObject jsonObjOutput = new JSONObject(apiOutput);
            returnresp = jsonObjOutput.toString();

            if (jsonObjOutput.has("Status")) {
                //failure in status for DataRange WS
                String returnStatus = jsonObjOutput.get("Status").toString();
                String returnMessage = jsonObjOutput.get("Message").toString();
                int a = Integer.parseInt(returnStatus);
                du.pushLog(instanceCode, secCode, ministryCode, deptCode, projectCode, groupId, start, end, returnMessage, date, date, a, a);
            } else {
                //success response for DataRange WS
                RetDMDashCaption = jsonObjOutput.get("RetDMDashCaption").toString();
                JSONArray jsonArray = new JSONArray(RetDMDashCaption);
                for (int i = 0; i < jsonArray.length(); i++) {
                    dateranges.add(new SimpleDateFormat("MM/dd/yyyy").parse(jsonArray.getJSONObject(i).get("datadate").toString()));
                }

                for (Date dr : dateranges) {
                    postRESTDashboard(dr, path);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return returnresp;
    }

    public String postRESTDashboard(Date date, String path) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        String encryptedString = "";
        String request_start = "", request_end = "", returnresp = "";
        Timestamp start;
        Timestamp end;
        int statusCode = 0;

        PMDarpanUtility du = new PMDarpanUtility();

        try {
            /*Create POST request for http://prayasapi.darpan.nic.in/pushdata */
            HttpPost postRequest = new HttpPost(PushDataAPIUrl);
            postRequest.addHeader("content-type", "application/json");

            //System.out.println("Final-"+du.getData(date, path));
            StringEntity userEntity = new StringEntity(du.getData(date, path));
            postRequest.setEntity(userEntity);

            start = du.getCurrentTimeStamp();
            HttpResponse response = httpClient.execute(postRequest);
            end = du.getCurrentTimeStamp();

            /*Checking Status Code*/
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201) {
                du.pushLog(instanceCode, secCode, ministryCode, deptCode, projectCode, groupId, start, end, "Request Failed with HTTP error code", date, date, 0, statusCode);
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }

            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);

            JSONObject jsonObjOutput = new JSONObject();
            jsonObjOutput = new JSONObject(apiOutput);
            System.err.println(jsonObjOutput);
            returnresp = jsonObjOutput.toString();

            if (jsonObjOutput.has("Status")) {
                //failure in dashboard WS

                String returnStatus = jsonObjOutput.get("Status").toString();
                String returnMessage = jsonObjOutput.get("Message").toString();
                System.err.println(returnStatus);
                System.err.println(returnMessage);
                int a = Integer.parseInt(returnStatus);
                du.pushLog(instanceCode, secCode, ministryCode, deptCode, projectCode, groupId, start, end, returnMessage, date, date, a, a);
            } else {
                du.pushLog(instanceCode, secCode, ministryCode, deptCode, projectCode, groupId, start, end, "Dashboard Service Error", date, date, statusCode, statusCode);
                return "error";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return returnresp;
    }

    public static void main(String [] args) throws FileNotFoundException {
        String fileName = "src/main/resources/sample.key";
        System.out.println("USER:DIR!:" + System.getProperty("user.dir"));
        File newFile = new File(fileName);
        if(!newFile.exists())
            throw new FileNotFoundException(fileName);
        else System.out.println("File "+ newFile.getAbsolutePath() +  "Present!");

        ConsumeWebService webService = new ConsumeWebService();
        webService.postRESTDashboard(new Date(), fileName);
    }
}
