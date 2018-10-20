package homestation.hospital;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;

import com.nexmo.client.sms.messages.TextMessage;
import homestation.HomestationSettings;

import java.io.IOException;

class SMSNotificator {

    private static AuthMethod auth = new TokenAuthMethod(HomestationSettings.NEXMO_API_KEY, HomestationSettings.NEXMO_API_SECRET);
    private static NexmoClient client = new NexmoClient(auth);
    private static String userPhone = HomestationSettings.PHONE_NUMBER_USER;

    static void sendSMS(String text) {

        //from costante (da account Nexmo), to è da prendere da DB ed è anche da verificare sul sito di Nexmo prima
        try {
            client.getSmsClient().submitMessage(new TextMessage(HomestationSettings.NEXMO_PHONE_NUMBER, userPhone, text));
            System.out.println("SMS inviato correttamente");
        } catch (NexmoClientException | IOException e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio dell'SMS");
        }
    }
}