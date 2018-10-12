package homestation.hospital;

import com.google.gson.Gson;
import smile.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;

class DistanceEvaluation {

    private static String home="", hospital="", distance="Tra_3_e_10_kilometri";

    static void calculateDistance(Network net) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(HospitalConstants.USER_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            HashMap map = new Gson().fromJson(result.toString(), HashMap.class);
            home = (String) map.get("home_address");
            hospital = (String) map.get("hospital_address");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //quello che segue va racchiuso tutto in un try-catch in cui si usano le API del GraphHopper
        System.out.println("Casa: " + home + "\nOspedale: " + hospital);

        //distance = "Meno_di_3_kilometri";
        distance = "Tra_3_e_10_kilometri";
        //distance = "Oltre_10_kilometri";

        net.setEvidence("Distanza", distance);
    }
}