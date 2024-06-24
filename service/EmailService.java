package com.smart.contact.service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public boolean sendEmail(String host, String port, final String userName, final String password, String toAddress,
                                 String subject, String message) throws AddressException, MessagingException {

    	boolean f=false;
        // Sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        MimeMessage msg = new MimeMessage(session);

       try {		    	   
           // Creates a new email message        
           msg.setFrom(userName);           
           msg.setRecipient(Message.RecipientType.TO,new InternetAddress(toAddress));
           msg.setSubject(subject);
           msg.setSentDate(new java.util.Date());
           msg.setText(message);

           // Sends the email
           Transport.send(msg);
           f=true;
	} catch (Exception e) {
		e.printStackTrace();
		

    }
return f;
  
    }

	private Address InternetAddress(String toAddress) {
		// TODO Auto-generated method stub
		return null;
	}
}

