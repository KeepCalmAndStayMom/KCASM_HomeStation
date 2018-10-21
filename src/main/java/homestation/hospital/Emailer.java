package homestation.hospital;

import homestation.HomestationSettings;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

class Emailer {

    private static String mailTo = HomestationSettings.EMAIL_USER;

    static void sendEmail(String content) {
        //to è da prendere da DB
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