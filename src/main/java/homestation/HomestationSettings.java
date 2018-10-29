package homestation;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class HomestationSettings {

    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");

    public static int HOMESTATION_ID;

    static int SENSOR_NODE;

    public static String FITBIT_URL;

    public static String API_KEY;
    public static String API_SECRET;
    public static int CALLBACK_PORT_FITBIT;
    public static String CALLBACK_FITBIT;

    //public static String API_URL = "http://172.30.1.138/api/C0vPwqjJZo5Jt9Oe5HgO6sBFFMxgoR532IxFoGmx";
    public static String API_URL;

    public static String IPADRESS_ZWAY;
    public static String USERNAME;
    public static String PASSWORD;

    public static String BROKER_URL;

    public static String EMAIL_KCASM;
    public static String PASSWORD_KCASM;

    public static String NEXMO_PHONE_NUMBER;
    public static String NEXMO_API_KEY;
    public static String NEXMO_API_SECRET;

    public static String EMAIL_USER;
    public static LocalDate PREGNANCY_START_DATE;
    public static String PHONE_NUMBER_USER;
    public static String HOME_ADDRESS;
    public static String HOSPITAL_ADDRESS;

    public static boolean EMAIL_NOTIFICATION;
    public static boolean SMS_NOTIFICATION;

    static void createSettings() {
        Map settings = new Gson().fromJson(jsonFromFile("src/main/resources/settings.json"), Map.class);

        HOMESTATION_ID = ((Double)settings.get("HOMESTATION_ID")).intValue();
        SENSOR_NODE = ((Double)settings.get("SENSOR_NODE")).intValue();
        FITBIT_URL = String.valueOf(settings.get("FITBIT_URL"));
        API_KEY = String.valueOf(settings.get("API_KEY"));
        API_SECRET = String.valueOf(settings.get("API_SECRET"));
        CALLBACK_PORT_FITBIT = ((Double)settings.get("CALLBACK_PORT_FITBIT")).intValue();
        CALLBACK_FITBIT = String.valueOf(settings.get("CALLBACK_FITBIT"));
        API_URL = String.valueOf(settings.get("API_URL"));
        IPADRESS_ZWAY = String.valueOf(settings.get("IPADRESS_ZWAY"));
        USERNAME = String.valueOf(settings.get("USERNAME"));
        PASSWORD = String.valueOf(settings.get("PASSWORD"));
        BROKER_URL = String.valueOf(settings.get("BROKER_URL"));
        EMAIL_KCASM = String.valueOf(settings.get("EMAIL_KCASM"));
        PASSWORD_KCASM = String.valueOf(settings.get("PASSWORD_KCASM"));
        NEXMO_PHONE_NUMBER = String.valueOf(settings.get("NEXMO_PHONE_NUMBER"));
        NEXMO_API_KEY = String.valueOf(settings.get("NEXMO_API_KEY"));
        NEXMO_API_SECRET = String.valueOf(settings.get("NEXMO_API_SECRET"));

        settings = new Gson().fromJson(jsonFromFile("src/main/resources/user_settings.json"), Map.class);

        EMAIL_USER = String.valueOf(settings.get("EMAIL_USER"));
        PREGNANCY_START_DATE = LocalDate.parse(String.valueOf(settings.get("PREGNANCY_START_DATE")));
        PHONE_NUMBER_USER = String.valueOf(settings.get("PHONE_NUMBER_USER"));
        HOME_ADDRESS = String.valueOf(settings.get("HOME_ADDRESS"));
        HOSPITAL_ADDRESS = String.valueOf(settings.get("HOSPITAL_ADDRESS"));
        EMAIL_NOTIFICATION = (boolean) settings.get("EMAIL_NOTIFICATION");
        SMS_NOTIFICATION = (boolean) settings.get("SMS_NOTIFICATION");
    }

    private static String jsonFromFile(String filePath) {
        StringBuilder builder = new StringBuilder();

        try {
            Scanner scan = new Scanner(new File(filePath));
        while(scan.hasNext())
            builder.append(scan.next());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

}
