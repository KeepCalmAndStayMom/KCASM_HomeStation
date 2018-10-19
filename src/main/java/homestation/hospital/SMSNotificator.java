package homestation.hospital;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;

import com.nexmo.client.sms.messages.TextMessage;

import java.io.IOException;

class SMSNotificator {

    static void sendSMS(String content) {
        AuthMethod auth = new TokenAuthMethod("635924f3", "577sIM8w7pdEYRyS");
        NexmoClient client = new NexmoClient(auth);

        //from costante (da account Nexmo), to è da prendere da DB ed è anche da verificare sul sito di Nexmo prima
        try {
            client.getSmsClient().submitMessage(new TextMessage("+393442298522", "+393347437264", content));
            System.out.println("SMS inviato correttamente");
        } catch (NexmoClientException | IOException e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio dell'SMS");
        }
    }
}