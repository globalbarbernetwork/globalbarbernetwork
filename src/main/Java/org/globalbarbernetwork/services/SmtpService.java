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
 * @author Grup 3
 */
public class SmtpService {
    private Properties props;
    
    public SmtpService() {
        props = new Properties();
        try {
            InputStream is = new FileInputStream("../../../resources/config.properties");
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
