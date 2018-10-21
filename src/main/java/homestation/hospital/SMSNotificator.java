package homestation.hospital;

import com.google.gson.Gson;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;

import com.nexmo.client.sms.messages.TextMessage;
import homestation.HomestationSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

class SMSNotificator {

    private static AuthMethod auth = new TokenAuthMethod(HomestationSettings.NEXMO_API_KEY, HomestationSettings.NEXMO_API_SECRET);
    private static NexmoClient client = new NexmoClient(auth);
    private static String userPhone = HomestationSettings.PHONE_NUMBER_USER;

    static void sendSMS(String text) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://localhost:4567/api/users/" + HomestationSettings.HOMESTATION_ID);//da modificare quando ci sarà il nuovo DB con le nuove API
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            HashMap<String, Object> map = new Gson().fromJson(result.toString(), HashMap.class);
            userPhone = (String) map.get("phone_number");
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Errore nel recupero del numero di telefono");
        }

        try {
            client.getSmsClient().submitMessage(new TextMessage(HomestationSettings.NEXMO_PHONE_NUMBER, userPhone, text));
            System.out.println("SMS inviato correttamente");
        } catch (NexmoClientException | IOException e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio dell'SMS");
        }
    }
}