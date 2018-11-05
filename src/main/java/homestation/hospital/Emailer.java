package homestation.hospital;

import com.google.gson.Gson;
import homestation.HomestationSettings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

class Emailer {
    private static String mailTo = HomestationSettings.EMAIL_USER;

    static void sendEmail(String content) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(HospitalConstants.LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            HashMap<String, Object> map = new Gson().fromJson(result.toString(), HashMap.class);
            mailTo = (String) map.get("email");
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Errore nel recupero della mail");
        }

        createAndSend(mailTo, content);
    }

    private static void createAndSend(String to, String msg) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(HomestationSettings.EMAIL_KCASM, HomestationSettings.PASSWORD_KCASM);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(HospitalConstants.SUBJECT);
            message.setText(msg);

            Transport.send(message);
            System.out.println("Mail inviata correttamente");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio della mail");
        }
    }
}