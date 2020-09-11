package darpan;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
//import static util.BaseHibernateDAO.closeSession;

public class PMDarpanUtility implements PMDarpanCode {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void pushLog(int instancecode, int seccode, int ministrycode, int deptcode, int projectcode, int groupId, Timestamp request_start, Timestamp request_end, String msg, Date datadt_f, Date datadt_t, int status, int errorcode) {

    }
    /*
    public void pushLog(int instancecode, int seccode, int ministrycode, int deptcode, int projectcode, int groupId, Timestamp request_start, Timestamp request_end, String msg, Date datadt_f, Date datadt_t, int status, int errorcode) {

        org.hibernate.Transaction tx;
        getSession().clear();
        tx = getSession().beginTransaction();
        try {
            Query q = getSession().createSQLQuery("Insert into pmdarpan_log (instance_code,sec_code,ministry_code,dept_code,project_code,group_id,req_start_dt,req_end_dt,msg,datadt_from,datadt_to,status,error_code)"
                    + " values(:instancecode,:seccode,:ministrycode,:deptcode,:projectcode,:groupId,:request_start,:request_end,:message,:datadt_f,:datadt_t,:status,:errorcode)");
            q.setParameter("instancecode", instancecode);
            q.setParameter("seccode", seccode);
            q.setParameter("ministrycode", ministrycode);
            q.setParameter("deptcode", deptcode);
            q.setParameter("projectcode", projectcode);
            q.setParameter("groupId", groupId);
            q.setParameter("request_start", request_start);
            q.setParameter("request_end", request_end);
            q.setParameter("message", msg);
            q.setParameter("datadt_f", datadt_f);
            q.setParameter("datadt_t", datadt_t);
            q.setParameter("status", status);
            q.setParameter("errorcode", errorcode);
            q.executeUpdate();
            tx.commit();
            tx = null;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.print("txerr" + e.getMessage());

        } finally {
            closeSession();
        }
    }
    */
    /*
    public List<PMDarpanData> getDarpanData(Date dateString, String path) throws Exception {
        org.hibernate.Transaction tx;
        getSession().clear();
        String s;
        Timestamp t;
        List<PMDarpanData> pmDarpanData = null;
        try {
            tx = getSession().beginTransaction();
            Query q = getSession().createQuery("from PMDarpanData d where d.dataDate=:dateString");
            q.setParameter("dateString", dateFormat.parse(dateFormat.format(dateString)));

            pmDarpanData = (List<PMDarpanData>) q.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSession();
        }
        return pmDarpanData;
    }
    */

    public static Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        Timestamp currenttime = new Timestamp(today.getTime());
        return currenttime;
    }

    public static String compress(String str) throws IOException {

        byte[] blockcopy = ByteBuffer
                .allocate(4)
                .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                .putInt(str.length())
                .array();
        ByteArrayOutputStream os = new ByteArrayOutputStream(str.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(str.getBytes());
        gos.close();
        os.close();
        byte[] compressed = new byte[4 + os.toByteArray().length];
        System.arraycopy(blockcopy, 0, compressed, 0, 4);
        System.arraycopy(os.toByteArray(), 0, compressed, 4,
                os.toByteArray().length);
        return Base64.getEncoder().encodeToString(compressed);
    }

    public static String decompress(String zipText) throws IOException {
        byte[] compressed = Base64.getDecoder().decode(zipText);
        if (compressed.length > 4) {
            GZIPInputStream gzipInputStream = new GZIPInputStream(
                    new ByteArrayInputStream(compressed, 4,
                            compressed.length - 4));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int value = 0; value != -1;) {
                value = gzipInputStream.read();
                if (value != -1) {
                    baos.write(value);
                }
            }
            gzipInputStream.close();
            baos.close();
            String sReturn = new String(baos.toByteArray(), "UTF-8");
            return sReturn;
        } else {
            return "";
        }
    }

    public List<PMDarpanModel> getDarpanModel(Date dateString, String path) throws Exception {
        /*
        List<PMDarpanData> pmdarpandata = getDarpanData(dateString, path);
        for (PMDarpanData tr : pmdarpandata) {
            KPIDetails kpiData = new KPIDetails();
            kpiData.setGroupId(tr.getGroupId());
            kpiData.setDatadate(dateFormat.format(tr.getDataDate()));
            kpiData.setLValue(tr.getLvl1Code() + "," + tr.getLvl2Code() + "," + tr.getLvl3Code());
            kpiData.setKValue(tr.getKpi1Data() + "," + tr.getKpi2Data() + "," + tr.getKpi3Data() + "," + tr.getKpi4Data() + "," + tr.getKpi5Data());
            ListKpidata.add(kpiData);
        }*/

        List<KPIDetails> ListKpidata = new ArrayList<KPIDetails>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        List<PMDarpanModel> records = new ArrayList<PMDarpanModel>();
        PMDarpanModel darpanModel = new PMDarpanModel();
        darpanModel.setInstanceCode(instanceCode);
        darpanModel.setSecCode(secCode);
        darpanModel.setProjectCode(projectCode);
        darpanModel.setMinistryCode(ministryCode);
        darpanModel.setDeptCode(deptCode);
        darpanModel.setFrequencyId(frequencyId);
        darpanModel.setAtmpt(atmpt);
        darpanModel.setKpiDetails(ListKpidata);
        records.add(darpanModel);
        return records;
    }
    /**/
    public String getData(Date date, String path) throws Exception {

        //1. Get the Darpan Model Data
        List<PMDarpanModel> darpanModel = getDarpanModel(date, path);

        //2. Serialize the list of KPIData to json string
        String jsonString = new Gson().toJson(darpanModel);

        //3. Compress the json string
        String compressData = compress(jsonString);

        //4. Encrypt the compressed string and assign to ProjrctKpiDetails Object
        String EncyptedData = EncryptData(compressData, path);

        //5. ProjrctKpiDetails is Serialized and converted to json string
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("Instance_Code", instanceCode);
        jsonObj.put("Sec_Code", secCode);
        jsonObj.put("Ministry_Code", ministryCode);
        jsonObj.put("Dept_Code", deptCode);
        jsonObj.put("Project_Code", projectCode);
        JSONObject ProjrctKpiDetails = new JSONObject();
        ProjrctKpiDetails.put("IP", jsonObj);
        ProjrctKpiDetails.put("EncyptedData", EncyptedData);
        jsonArray.put(ProjrctKpiDetails);

        return jsonArray.toString();
    }

    /**/
    private String DecryptData(String str, String path) throws Exception {
        byte[] key = null;
        FileInputStream fileinputstream = new FileInputStream(path);
        key = new byte[fileinputstream.available()];
        fileinputstream.read(key);
        fileinputstream.close();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        byte[] keyBytes = new byte[16];
        int len = key.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(key, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] strByte = Base64.getDecoder().decode(str);
        byte[] plainBytesDecrypted = cipher.doFinal(strByte);

        return new String(plainBytesDecrypted);
    }

    private String EncryptData(String str, String path) throws Exception {

        byte[] key = null;
        FileInputStream fileinputstream = new FileInputStream(path);
        key = new byte[fileinputstream.available()];
        fileinputstream.read(key);
        fileinputstream.close();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
        byte[] keyBytes = new byte[16];
        int len = key.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(key, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] results = cipher.doFinal(str.getBytes("UTF-8"));

        String encData = Base64.getEncoder().encodeToString((results));
        return encData;
    }

    public static void main(String [] args) {
        System.out.println("Hello World");
    }
}
