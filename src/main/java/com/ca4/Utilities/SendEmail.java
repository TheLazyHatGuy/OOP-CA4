package com.ca4.Utilities;

/*
Taken from Cameron's CA3
 */

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail
{
    public static void sendRegistrationConfimation(String email) throws MessagingException {
        String[] to = {email};
        String subject = "Thank you for registering to GD2a Movie DB";
        String body = "<h1>" + subject + "</h1>\n" +
                "<p>This is to confirm that you have successfully registered with GD2a Movie DB</p>\n" +
                "<p>We hope you enjoy our service</p>\n";

        sendFromFTS(to, subject, body);
    }

    private static void sendFromFTS(String[] to, String subject, String body) throws MessagingException
    {
        System.out.println("Sending email...");

        String user = "moviedb@flyingtoilet.co.uk";
        String password = "Pa$$w0rd";
        Properties props = System.getProperties();
        String host = "mail.flyingtoilet.co.uk";

        props.setProperty("mail.smtp.starttls.enable", "false");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.user", user);
        props.setProperty("mail.smtp.password", password);
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(user));
        InternetAddress[] toAddress = new InternetAddress[to.length];

        for (int i = 0; i < toAddress.length; i++) {
            toAddress[i] = new InternetAddress(to[i]);
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }

        message.setSubject(subject);
        message.setContent(body, "text/html");
        Transport transport = session.getTransport("smtp");
        transport.connect(host, user, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

        System.out.println("Email sent");
    }
}
