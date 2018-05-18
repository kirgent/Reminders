package tv.zodiac.dev;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

/**
 * we are as Middle: send requests to AMS and got responses
 * Middle -> AMS -> box -> AMS -> Middle
 */
public class API {

    Boolean show_info_level = true;
    Boolean show_debug_level = false;
    Boolean show_generated_json = false;
    Boolean show_response_body = false;

    //private final static Logger log = Logger.getLogger(API.class.getName());

    enum Operation { add, modify, delete, purge, blablabla }

    //static Logger log = Logger.getLogger(testAMS.class.getName());
    //FileHandler txtFile = new FileHandler ("log.log", true);
    //private FileHandler fh = new FileHandler("test_reminder.log");

    final String charterapi_a = "http://spec.partnerapi.engprod-charter.net/api/pub";
    final String charterapi_b = "http://specb.partnerapi.engprod-charter.net/api/pub";
    final String charterapi_c = "http://specc.partnerapi.engprod-charter.net/api/pub";
    final String charterapi_d = "http://specd.partnerapi.engprod-charter.net/api/pub";
    final String postfix_settings = "/networksettingsmiddle/ns/settings";
    final String charterapi = charterapi_b;

    final String expected200 = "200 OK";
    final String expected201 = "201 Created";
    final String expected400 = "400 Bad Request";
    final String expected404 = "404 Not Found";
    final String expected500 = "500 Internal Server Error";
    final String expected504 = "504 Server data timeout";

    final String mac_wrong = "123456789012";
    final String boxD101 = "A0722CEEC970";//WB20 D101 ???
    final String boxD102 = "3438B7EB2E24";//WB20 D102
    final String boxD103 = "3438B7EB2E28";//WB20 D103
    final String boxD104 = "3438B7EB2E34";//WB20 D104
    final String boxD105 = "3438B7EB2E30";//WB20 D105
    final String boxD106 = "3438B7EB2EC4";//WB20 D106
    final String box_Kirmoto = "0000007F8214";//Kirmoto
    final String box_Tanya = "000005FE680A";//Tanya
    final String box_Vitya = "A0722CEEC934";//Vitya
    final String box_Katya_V = "0000048D4EB4";//Katya_V
    final String boxD111 = "2c7e81ee2530";
    final String box4210 = "A0722CEEC9B4";// 30.255.241.239  /  10.15.199.182
    final String box4212 = "A0722CEEC9A4";
    final String boxMoto2145_173 =  "000004B9419F"; //"B077AC5D91DD"; // "000004B9419F"; //Moto_2145_Mondo_DCX3200M_17.3_346
    final String boxMoto2147_Rems = "000004D67F70"; //000004d67f70"; //Moto_2147_Mondo_DCX3200M_REMS

    String mac = boxD102;

    ArrayList reminderScheduleId_list = new ArrayList();
    ArrayList reminderId_list = new ArrayList();
    ArrayList<Integer> add_avg_list = new ArrayList<>();
    ArrayList<Integer> modify_avg_list = new ArrayList<>();
    ArrayList<Integer> delete_avg_list = new ArrayList<>();
    ArrayList<Integer> purge_avg_list = new ArrayList<>();

    //CHANNELS
    int reminderChannelNumber = reminderChannelNumber();
    //int reminderChannelNumber_for_statuscode3 = 9999;
    //int reminderChannelNumber_for_statuscode4 = 1000;
    /*private Integer[] rack_channel30 = { 2, 3, 4, 5, 6, 7, 8, 9, 12, 13,
            14, 16, 18, 19, 22, 23, 25, 28, 30, 31,
            32, 33, 37, 38, 41, 44, 46, 48, 49, 50 };*/
    //Integer[] rack_channel = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    //@Deprecated
    final String reminderProgramId = "EP002960010113";

    String reminderProgramStart = reminderProgramStart();

    int reminderOffset = reminderOffset();
    //int reminderOffset_new = reminderOffset();
    long reminderScheduleId = reminderScheduleId();
    long reminderId = reminderId();

    int count_reminders = 2;

    private static String[] statuscode = {
            "0 - requested operation with the reminder was accomplished successfully. Always returned for \"Reminders Purge\" request (Request ID=3)",
            "1 - the number of reminders for the STB exceeded the limitation Applies to \"Reminders Add\" request (Request ID=0)",
            "2 - reminder is set for time in the past. Applies to \"Reminders Add\" request (Request ID=0)",
            "3 - reminder is set for unknown channel. \"Reminders Add\" request (Request ID=0)",
            "4 - reminder is unknown. Applies to \"Reminders Delete\" request (Request ID=1) and \"Reminders Modify\" request (Request ID=2)",
            "5 - reminder with provided pair of identifiers (reminderScheduleId and reminderId) is already set \"Reminders Add\" request (Request ID=0)"};

    final String ams_ip_4 = "172.30.81.4";
    final String ams_ip_19 = "172.30.112.19";
    final String ams_ip_132 = "172.30.82.132";
    String ams_ip = ams_ip_4;
    int ams_port = 8080;

    int get_average_time(ArrayList list) {
        int average = 0;
        if (list.size() > 0) {
            int sum = 0;
            for (int j = 0; j < list.size(); j++) {
                //System.out.println("[DBG] list_time.get(" + j + ")=" + list_time.get(j));
                sum = sum + (int)list.get(j);
            }
            average = sum / list.size();
        }
        return average;
    }

    int get_min_time(ArrayList list) {
        int min = 0;
        if (list.size() > 0) {
            min = (int)list.get(0);
            for (int j = 0; j < list.size(); j++) {
                if ((int)list.get(j) < min) {
                    min = (int) list.get(j);
                }
            }
        }
        return min;
    }

    int get_max_time(ArrayList list) {
        int max = 0;
        if (list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                if ((int)list.get(j) > max) {
                    max = (int) list.get(j);
                }
            }
        }
        return max;
    }

    @Deprecated
    String generate_json_test(String date, int count_reminders, String operation, int reminderOffset) {
        System.out.println("[DBG] [date] Generate_json: with date=" + date + ", " +
                "count_reminders=" + count_reminders + ", " +
                "operation=" + operation + ", " +
                "reminderOffset=" + reminderOffset);
         /*
        //WORKING parsing from json_string to Class:
        Gson g = new Gson();
        Reminder reminder = g.fromJson(json_add2, Reminder.class);
        System.out.println("[DBG] parsing from json_string to Class: \nmac: " + reminder.deviceId);
        System.out.println("[DBG] count of reminders in class: " + reminder.reminders.size());
        for(Reminders rems : reminder.reminders){System.out.println(
                    "operation: " + rems.operation + ", " +
                    "reminderChannelNumber: " + rems.reminderChannelNumber + ", " +
                    "reminderProgramStart: " + rems.reminderProgramStart + ", " +
                    "reminderProgramId: " + rems.reminderProgramId + ", " +
                    "reminderOffset: " + rems.reminderOffset);
            }

        //parsing from Class to json_string
        System.out.println("[DBG] parsing from Class to json_string: \n" + g.toJson(reminder));
*/
/*        //WORKING variant for one class Reminder + one class Reminders
        //==============================================================
        final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        //from class -> to string json
        //class with fields:
        Reminders rs = new Reminders(operation, reminderChannelNumber, reminderProgramStart, reminderProgramId, reminderOffset);
        Reminder r = new Reminder(mac, rs);
        //create json structure:
        String json = GSON.toJson(r);
        System.out.println("[DBG] from class -> to string json:\n" + json);

        //from json string -> to class
        Reminder to_class = GSON.fromJson(json, Reminder.class);
        System.out.println("[DBG] from json string -> to class:\n" + to_class.getDeviceId()+ " " + to_class.getClass());
*/
/*
        //from class -> to string json
        Reminder from_class = new Reminder(mac, Arrays.asList("operation", "reminderChannelNumber", "reminderProgramStart", "reminderProgramId", "reminderOffset"));
        String json = GSON.toJson(from_class);
        System.out.println("[DBG] from class -> to string json:\n" + json);

        //from json string -> to class
        Reminder to_class = GSON.fromJson(json, Reminder.class);
        System.out.println("[DBG] from json string -> to class:\n" + to_class.getDeviceId()+ " " + to_class.getReminders_list());
*/
/*
        //WORKING
        JsonObject jo = new JsonParser().parse(json_add2).getAsJsonObject();
        System.out.println("1 show full jsonobject: " + jo);
        show only jsonarray:
        String ja = jo.get("reminders").getAsJsonArray().toString();
        System.out.println("2 only jsonarray: " + ja);
*/
    return "";
    }

    String check_body_response(String body, String mac) {
        String result = "";
        if(body.contains("\"statusCode\":1")){
            //log.warning("one or more statusCode's = " + statuscode[1]);
            System.out.println("! one or more statusCode's = " + statuscode[1]);
            result += "1";
        }
        if(body.contains("\"statusCode\":2")){
            //log.warning("one or more statusCode's = " + statuscode[2]);
            System.out.println("! one or more statusCode's = " + statuscode[2]);
            result += "2";
        }
        if(body.contains("\"statusCode\":3")){
            //log.warning("one or more statusCode's = " + statuscode[3]);
            System.out.println("! one or more statusCode's = " + statuscode[3]);
            result += "3";
        }
        if(body.contains("\"statusCode\":4")){
            //log.warning("one or more statusCode's = " + statuscode[4]);
            System.out.println("! one or more statusCode's = " + statuscode[4]);
            result += "4";
        }
        if(body.contains("\"statusCode\":5")){
            //log.warning("one or more statusCode's = " + statuscode[5]);
            System.out.println("! one or more statusCode's = " + statuscode[5]);
            result += "5";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Request not accomplished\"")){
            result += "Request not accomplished";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-ST-001 Box is not registered\"")){
            result += "REM-ST-001 Box is not registered";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"unknown MAC\"")){
            result += "unknown MAC";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"STB not available\"")){
            result += "STB not available";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-002 Reminders Service error: REM-112\"")){
            result += "REM-002 Reminders Service error: REM-012 [" + mac + "] Request not accomplished";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Timeout detected by BoxResponseTracker")){
            result += "Timeout detected by BoxResponseTracker";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: missing program start\"")){
            result += "REM-008 Reminders parsing error: missing program start";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: invalid program start\"")){
            result += "REM-008 Reminders parsing error: invalid program start";
        }
        //if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: missing channel number\"")){
            //result += "REM-008 Reminders parsing error: missing channel number";
        //}
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: invalid channel number\"")){
            result += "REM-008 Reminders parsing error: invalid channel number";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: missing offset\"")){
            result += "REM-008 Reminders parsing error: missing offset";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: invalid offset\"")){
            result += "REM-008 Reminders parsing error: invalid offset";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: incorrect reminderScheduleId\"")){
            result += "REM-008 Reminders parsing error: incorrect reminderScheduleId";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"REM-008 Reminders parsing error: wrong number of reminders\"")){
            result += "REM-008 Reminders parsing error: wrong number of reminders";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Incorrect request: ChangeReminders\"")){
            result += "Incorrect request: ChangeReminders";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"Incorrect request: blablabla\"")){
            result += "Incorrect request: blablabla";
        }
        if(body.contains("\"status\":\"Failed\"") && body.contains("\"errorMessage\":\"name cannot be null\"")){
            result += "name cannot be null";
        }
        if(body.contains("REM-002 Reminders Service error: Can not connect to STB with stbId=" + mac)){
            result += "REM-002 Reminders Service error: Can not connect to STB with stbId=" + mac;
        }
        if(body.contains("REM-008 Reminders parsing error: wrong deviceId")){
            result += "REM-008 Reminders parsing error: wrong deviceId";
        }
        if(body.contains("REM-008 Reminders parsing error: wrong operation")){
            result += "REM-008 Reminders parsing error: wrong operation";
        }
        if(body.contains("REM-008 Reminders parsing error: incorrect message format")){
            result += "REM-008 Reminders parsing error: incorrect message format";
        }
        if(body.contains("REM-008 Reminders parsing error: incorrect reminderId")){
            result += "REM-008 Reminders parsing error: incorrect reminderId";
        }
        if(body.contains("Failed to getAmsIpBymac for : " + mac + ", with error: No amsIp found for mac: STB" +mac)){
            result += "No amsIp found for mac";
        }
        if(body.contains("STB MAC not found: " + mac)){
            result += "STB MAC not found: " + mac;
        }
        if(body.contains("incorrect value")){
            result += "incorrect value";
        }
        if(body.contains("SET-025 Unsupported data type: Not a JSON Object:")){
            result += "SET-025 Unsupported data type: Not a JSON Object";
        }
        if(body.contains("responseCode\":\"ERROR_SCHEDULING_REMINDER")){
            result += "ERROR_SCHEDULING_REMINDER";
        }
        //if(Objects.equals(result, "")){
            //result = " ";
        //}
        //System.out.println("[DBG] check_body_for_statuscode: result: " + result);
        return result;
    }

    StringBuilder read_response(StringBuilder body, HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        //StringBuilder body = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            if(show_response_body) {
                System.out.print(", response body: " + body.append(line));
            }else{
                body.append(line);
            }
            if (reader.readLine() == null) {
                System.out.println();
            }
        }
        return body;
    }

    HttpGet prepare_get_request(String uri) {
        HttpGet request = new HttpGet(uri);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Cache-Control", "no-cache");
        System.out.println("[DBG] request string: " + request);
        return request;
    }

    HttpPost prepare_post_request(String uri){
        HttpPost request = new HttpPost(uri);
        System.out.println("[DBG] request string: " + request);
        return request;
    }

    /*@Deprecated
    ArrayList Purge(String ams_ip, String mac) throws IOException {
        System.out.println("Purge for ams_ip=" + ams_ip + " and mac=" + mac);
        //log.info("Purge for ams_ip=" + ams_ip + " and mac=" + mac);

        String url = "http://" + ams_ip + ":" + ams_port + postfix_change;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        String json_purge = "{\"deviceId\":" + mac + ",\"reminders\":[{\"operation\":\"Purge\"}]}";
        request.setEntity(new StringEntity(json_purge));
        //request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        System.out.println("[DBG] Request string: " + request);
        //+ "\n[DBG] Request entity: " + request.getEntity());

        long start = currentTimeMillis();
        HttpResponse response = client.execute(request);
        long finish = currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms request, " +
                "Response getStatusLine: " + response.getStatusLine());

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder body = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null; ) {
            body.append(line);
            //System.out.println("[DBG] Response body: " + body.append(line).append("\n"));
        }

   /*     // Считываем json
        Object obj = new JSONParser().parse(json_purge); // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
        // Кастим obj в JSONObject
        JSONObject jo = (JSONObject) obj;
        // Достаём firstName and lastName
        String firstName = (String) jo.get("firstName");
        String lastName = (String) jo.get("lastName");
        System.out.println("fio: " + firstName + " " + lastName);
        // Достаем массив номеров
        JSONArray phoneNumbersArr = (JSONArray) jo.get("phoneNumbers");
        Iterator phonesItr = phoneNumbersArr.iterator();
        System.out.println("phoneNumbers:");
        // Выводим в цикле данные массива
        while (phonesItr.hasNext()) {
            JSONObject test = (JSONObject) phonesItr.next();
            System.out.println("- type: " + test.get("type") + ", phone: " + test.get("number"));
        }*/

   /*     ArrayList arrayList = new ArrayList();
        arrayList.add(0, response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
        arrayList.add(1, check_body_for_statuscode(body.toString()));
        client.close();
        return arrayList;
    }*/

    ArrayList QueryDB(String ams_ip, String mac) throws ClassNotFoundException, SQLException {
        //ResultSet QueryDB(String mac) throws ClassNotFoundException, SQLException {
        System.out.println("QueryDB for mac=" + mac + " to DB on AMS=" + ams_ip);

        String url = "jdbc:oracle:thin:@//ams-db01.enwd.co.sa.charterlab.com:1521/zdev02";
        String username = "ams_ipv6_e591";
        String password = "ams_ipv6_e591";

        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        long start = currentTimeMillis();
        ResultSet result = statement.executeQuery("select * from MAC_IP where MAC_STR = '" + mac + "\'");
        long finish = currentTimeMillis();
        System.out.println("[DBG] " + (finish - start) + "ms query");

        ArrayList actual = new ArrayList();
        while (result.next()) {
            actual.add(result.getLong(1));
            actual.add(result.getLong(2));
            actual.add(result.getInt(3));
            actual.add(result.getString(4));
            actual.add(result.getString(5));
        }
        if(!actual.isEmpty()) {
            System.out.println("[DBG] return result: "
                    + actual.get(0) + "  "
                    + actual.get(1) + "  "
                    + actual.get(2) + "  "
                    + actual.get(3) + "  "
                    + actual.get(4));
        }
        connection.close();
        return actual;
    }

    /**
     * @return just return the day=tomorrow: yyyy-mm-dd
     */
    String reminderProgramStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_YEAR, +1);
        return pattern.format(calendar.getTime());
    }

    int reminderChannelNumber() {
        return Math.abs(new Random().nextInt(1000));
    }

    int reminderOffset() {
        return Math.abs(new Random().nextInt(100));
    }

    @Deprecated
    void printArrayList(ArrayList list){
        if(list != null)
        {
            for(Object a : list)
                if(a != null) System.out.println("element:" + a);
        }
    }

    @Deprecated
    static Boolean ContainsAllNulls(ArrayList list)
    {
        if(list != null)
        {
            for(Object a : list)
                if(a != null) return false;
        }
        return true;
    }

    long reminderScheduleId(){
        Random random = new Random();
        long reminderScheduleId = Math.abs(random.nextLong());
        reminderScheduleId_list.add(reminderScheduleId);
        //if(show_extra_info) {
            //System.out.println("reminderScheduleId_list<-add=" + reminderScheduleId);
        //}
        return reminderScheduleId;
    }

    long reminderId(){
        Random random = new Random();
        long reminderId = Math.abs(random.nextLong());
        reminderId_list.add(reminderId);
        //if(show_extra_info) {
            //System.out.println("reminderId_list<-add        =" + reminderId);
        //}
        return reminderId;
    }


    String get_date(int index) {
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder result = new StringBuilder();

        calendar.add(Calendar.DAY_OF_YEAR, +index);
        result = result.append(pattern.format(calendar.getTime()));

        System.out.println("generated date: " + result);
        return result.toString();
    }

    String get_date_full(int index) {
        //Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, -1);
        //System.out.println("Yesterday's date = "+ cal.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder result = new StringBuilder();

        calendar.add(Calendar.DAY_OF_YEAR, +index);
        result = result.append(pattern.format(calendar.getTime()));

        System.out.println("generated date_full: " + result);
        return result.toString();
    }

    @Deprecated
    String get_date_several(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= count; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, +1);
            result.append(pattern.format(calendar.getTime()));
            if (i != count) {
                result.append(" ");
            }
        }
        System.out.println("generated date: " + result);
        return result.toString();
    }

    @Deprecated
    String get_time_old(int count) {
        int interval_in_minutes;
        if (count<=48){ interval_in_minutes = 30; }
        else if (count<=288){ interval_in_minutes = 5; }
        else if (count<=720){ interval_in_minutes = 2; }
        else interval_in_minutes = 1;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("HH:mm");
        calendar.setTime(new java.util.Date(0, 0, 0, 0, 0));

        StringBuilder result = new StringBuilder();
        for (int i=1; i<=count; i++){
            result.append(pattern.format(calendar.getTime()));
            calendar.add(Calendar.MINUTE, interval_in_minutes);
            if(i!=count){
                result.append(" ");
            }
        }
        System.out.println("generated times: " + result);
        return result.toString();
    }

    String get_time(int count, int number) {
        int interval_in_minutes;
        if (count<=48){ interval_in_minutes = 30; }
        else if (count<=288){ interval_in_minutes = 5; }
        else if (count<=720){ interval_in_minutes = 2; }
        else interval_in_minutes = 1;

        if (number < 1) { number = 1; }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("HH:mm");
        calendar.setTime(new Date(0, 0, 0, 0, 0));
        calendar.add(Calendar.MINUTE, interval_in_minutes*(number-1));
        String result = pattern.format(calendar.getTime());
        System.out.println("generated time: " + result);
        return result;
    }

    String get_time(int i) {
        //int interval_in_minutes;
        //if (count<=48){ interval_in_minutes = 30; }
        //else if (count<=288){ interval_in_minutes = 5; }
        //else if (count<=720){ interval_in_minutes = 2; }
        //else interval_in_minutes = 1;

        //if (number < 1) { number = 1; }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pattern = new SimpleDateFormat("HH:mm");
        //calendar.setTime(new Date(0, 0, 0, 0, 0));
        calendar.add(Calendar.MINUTE, i);
        String result = pattern.format(calendar.getTime());
        System.out.println("generated time: " + result);
        return result;
    }

    String get_date_time(int i){
        int count_rems_in_day = 1440;
        //int count_full_days = count_reminders / count_rems_in_day;
        //int ostatok = count_reminders - (count_full_days * count_rems_in_day);
        //System.out.println("count_full_days=" + count_full_days + ", ostatok=" + ostatok);

        String result = get_date((i/count_rems_in_day)+1) + " " + get_time(i);
        //String result = get_date_full(i);

        System.out.println("generated date_time: " + result + "\n");
        return result;
    }

    String prepare_url(String ams_ip, Enum<Operation> operation, boolean newapi) {
        String result;
        if (newapi) {
            result = "http://" + ams_ip + ":" + ams_port + "/ams/Reminders?req=" + operation;
        } else {
            result = "http://" + ams_ip + ":" + ams_port + "/ams/Reminders?req=ChangeReminders";
        }
        return result;
    }

}