package com.ca4.Utilities;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail
{
    public static void main(String[] args)
    {
        String[] to = {"admin@flyingtoilet.co.uk"};
        String subject = "Your Car Booking";
        String body = "Thanks for choosing GD2 cars";
        sendFromFTS(to, subject, body);
    }

    private static void sendFromFTS(String[] to, String subject, String body)
    {
        System.out.println("Sending email...");

        String user = "teslaco@flyingtoilet.co.uk";
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

        try
        {
            message.setFrom(new InternetAddress(user));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            for (int i = 0; i < toAddress.length; i++)
            {
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
        catch (AddressException e)
        {
            System.out.println("SendEmail.sendFromFTS.AddressException");
            e.printStackTrace();

        }
        catch (MessagingException e)
        {
            System.out.println("SendEmail.sendFromFTS.MessagingException");
            e.printStackTrace();
        }
    }
}
