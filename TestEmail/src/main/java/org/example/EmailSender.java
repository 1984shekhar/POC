package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
public class EmailSender {
    public static void sendEmail(String host, String port, final String username, final String password, String toAddress, String subject, String message) {
        // Set up the SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            // Create a default MimeMessage object
            Message mimeMessage = new MimeMessage(session);
            // Set From: header field
            mimeMessage.setFrom(new InternetAddress(username));
            // Set To: header field
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            // Set Subject: header field
            mimeMessage.setSubject(subject);
            // Set the actual message
            mimeMessage.setText(message);
            // Send message
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // Retrieve configuration from system properties
        String host = System.getProperty("mail.smtp.host");
        System.out.println("host: "+host);
        String port = System.getProperty("mail.smtp.port");
        String username = System.getProperty("mail.smtp.username");
        String password = System.getProperty("mail.smtp.password");
        String toAddress = System.getProperty("mail.to.address", "recipient@example.com"); // Default recipient
        String subject = System.getProperty("mail.subject", "Test Email");
        String message = System.getProperty("mail.message", "This is a test email from Java program");
        // Ensure all required properties are provided
        if (host == null || port == null || username == null || password == null) {
            System.err.println("Please provide all required system properties: mail.smtp.host, mail.smtp.port, mail.smtp.username, mail.smtp.password");
            return;
        }
        sendEmail(host, port, username, password, toAddress, subject, message);
    }
}