package dev.codecounty.acebank.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.java.Log;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Log
public class MailUtil {

    /**
     * Sends mail asynchronously so the UI doesn't freeze.
     */
    public static void sendMailAsync(String recipient, String subject, String body) {
        CompletableFuture.runAsync(() -> {
            try {
                sendMail(recipient, subject, body);
            } catch (Exception e) {
                log.warning("Background email failed: " + e.getMessage());
            }
        });
    }

    public static boolean sendMail(final String recipient, String subject, String body) throws MessagingException {
        log.info("Attempting to send email to: " + recipient);

        try {
            Session session = Session.getInstance(getProperties(), new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            ConfigLoader.getProperty(ConfigKeys.MAIL_ADDR),
                            ConfigLoader.getProperty(ConfigKeys.MAIL_PWD)

                    );
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.DEFAULT_MAIL, "AceBank Support"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Using setContent to support potential HTML formatting later
            message.setText(body);

            Transport.send(message);
            log.info("Email sent successfully to " + recipient);
            return true;

        } catch (Exception e) {
            log.severe("Failed to send email: " + e.getMessage());
            // In a bank app, we usually log and continue rather than crashing the whole process
            return false;
        }
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Timeout settings (Crucial so your app doesn't hang if Gmail is slow)
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");

        return props;
    }
}