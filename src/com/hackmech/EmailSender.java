package com.hackmech;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void sendEmail(String toEmail, String subject, String body) throws IOException {
        //load details from config file
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));

        String uname = props.getProperty("gmail.uname");
        String password = props.getProperty("gmail.password");

        // SMTP configuration
//        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(uname, password);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(uname));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send email
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
