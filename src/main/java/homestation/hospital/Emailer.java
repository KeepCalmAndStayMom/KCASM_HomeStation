package homestation.hospital;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

class Emailer {
    static void sendEmail(String content) {
        //from e password da account Gmail, to è da prendere da DB e il resto dipende dalle costanti in HospitalConstants
        createAndSend("kandstaymom@gmail.com", "KCASM_96", "paul.lamberto@hotmail.com", HospitalConstants.SUBJECT, content);
    }

    private static void createAndSend(String from, String password, String to, String sub, String msg) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);

            Transport.send(message);
            System.out.println("Mail inviata correttamente");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Errore nell'invio della mail");
        }
    }
}