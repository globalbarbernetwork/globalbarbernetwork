/*
 * Copyright (C) 2020 Grup 3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.globalbarbernetwork.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.globalbarbernetwork.constants.Constants;

/**
 *
 * @author Grup 3
 */
public class SmtpService {

    private Properties props;

    /**
     *
     * It is a constructor that initialize a properties for read configuration
     * and variables
     *
     */
    public SmtpService() {

        props = new Properties();
        try {
            InputStream is = new FileInputStream(Constants.PATH_PROPERTIES);
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * Send email
     *
     * @param recipient the recipient
     * @param subject the subject
     * @param body the body
     */
    public void sendEmail(String recipient, String subject, String body) {

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        Transport transport = null;
        String sender = props.getProperty("mail.smtp.user");

        try {
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));   //Se podrían añadir varios de la misma manera
            message.setSubject(subject);
            message.setContent(body, "text/html");

            transport = session.getTransport("smtp");
            transport.connect(props.getProperty("mail.smtp.host"), sender, props.getProperty("mail.smtp.password"));
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
    
    /**
     *
     * Send email with attachment
     *
     * @param recipient the recipient
     * @param subject the subject
     * @param body the body
     * @param attachment the file to attach
     */
    public void sendEmailWithAttachment(String recipient, String subject, String body, File attachment) {

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        Transport transport = null;
        String sender = props.getProperty("mail.smtp.user");

        try {
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));   //Se podrían añadir varios de la misma manera
            message.setSubject(subject);

            // Se crea cuerpo del mensaje
            BodyPart messageBodyText = new MimeBodyPart();
            messageBodyText.setContent(body, "text/html");

            // Se crea parte del adjunto
            MimeBodyPart messageBodyAttachment = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            messageBodyAttachment.setDataHandler(new DataHandler(source));
            messageBodyAttachment.setFileName(attachment.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyText);
            multipart.addBodyPart(messageBodyAttachment);

            message.setContent(multipart);
            
            transport = session.getTransport("smtp");
            transport.connect(props.getProperty("mail.smtp.host"), sender, props.getProperty("mail.smtp.password"));
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
