/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.xtec.ioc.firebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author DAW IOC
 */
public class SmtpService {
    private Properties props;
    
    public SmtpService() {
        props = new Properties();
        try {
            InputStream is = new FileInputStream("./src/main/resources/config.properties");
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void sendEmail(String recipient, String subject, String body) {
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        Transport transport = null;
        String sender = props.getProperty("mail.smtp.user");

        try {
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));   //Se podrían añadir varios de la misma manera
            message.setSubject(subject);
            message.setText(body);

            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", sender, props.getProperty("mail.smtp.password"));
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();   //Si se produce un error
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
