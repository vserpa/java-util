package br.com.eblade.java.util.email;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
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
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

public class SMTPUtil {
	
	private static final Logger log = Logger.getLogger(SMTPUtil.class);
	
	public static void sendMail(Email email, Properties props) throws UnsupportedEncodingException, MessagingException, IOException {

		if(log.isDebugEnabled()) {
			log.debug("static void SMTPUtil.sendMail(Email email, Properties props)");
			log.debug(email);
			log.debug(props);
		}
		
		// mail session
		Session session = Session.getDefaultInstance(new Properties(), null);
		MimeMessage message = new MimeMessage(session);
		
		// from
		InternetAddress from = new InternetAddress();
		from.setAddress(email.getFromAddress());
		from.setPersonal(email.getFromPersonal());
		message.setFrom(from);
		
		// to
		for (String to : email.getRecipients()) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}
		
		// carbon copy
		for (String carbonCopy : email.getCc()) {
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(carbonCopy));
		}
		
		// blind carbon copy
		for (String blindCarbonCopy : email.getBcc()) {
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(blindCarbonCopy));
		}
		
		// subject
		message.setSubject(email.getSubject(), "iso-8859-1");

		// multipart email
		MimeMultipart multipart = new MimeMultipart("related");
		
		// content
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(email.getBody(), email.getContentType());
		multipart.addBodyPart(messageBodyPart);

		// attachments
		for (Attachment attachment : email.getAttachments()) {
			DataSource dataSource = null;
			if(attachment.getType().equals(Attachment.Type.File)) {
				dataSource = new FileDataSource(attachment.getAttachmentAsFile());
			} else if(attachment.getType().equals(Attachment.Type.InputStream)) {
				dataSource = new ByteArrayDataSource(attachment.getAttachmentAsInputStream(), attachment.getContentType());
			}
			
			if(dataSource != null) {
				MimeBodyPart attach = new MimeBodyPart();
				attach.setDataHandler(new DataHandler(dataSource));
				attach.setFileName(attachment.getName());
				multipart.addBodyPart(attach);
			}
		}
		
		// content ids
		for (Attachment attachment : email.getContentIds()) {
			MimeBodyPart cid = new MimeBodyPart();
			DataSource dataSource = new ByteArrayDataSource(attachment.getAttachmentAsInputStream(), attachment.getContentType());			
			cid.setDataHandler(new DataHandler(dataSource));
			cid.setHeader("Content-ID", attachment.getName());
	        multipart.addBodyPart(cid);
		}
		
		// save message
		message.setContent(multipart);
		message.saveChanges();
		
		sendMail(message, props);
	}
	
	
	public static void sendMail(Message message, Properties props) throws UnsupportedEncodingException, MessagingException {

		if(log.isDebugEnabled()) {
			log.debug("static void SMTPUtil.sendMail(Email email, Properties props)");
			log.debug(message);
			log.debug(props);
		}
		
		// mail session
		SimpleAuth auth = new SimpleAuth(props.getProperty("mail.user"), props.getProperty("mail.pwd"));
		Session session = Session.getInstance(props, auth);
		
		// send message
        Transport transport = session.getTransport("smtp");
        transport.connect(props.getProperty("mail.smtp.host"), props.getProperty("mail.user"), props.getProperty("mail.pwd"));
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
	}
	
	public static void forwardMail(Message message, Properties props, String fromAddress, String toAddress, String subject, String body)  throws UnsupportedEncodingException, MessagingException {
		if(log.isDebugEnabled()) {
			log.debug("void forwardMail(Message message, Properties props, String fromAddress, String toAddress)");
			log.debug(message);
			log.debug(props);
			log.debug(fromAddress);
			log.debug(toAddress);
		}
		
		// mail session
		SimpleAuth auth = new SimpleAuth(props.getProperty("mail.user"), props.getProperty("mail.pwd"));
		Session session = Session.getInstance(props, auth);
		
		// Create the message to forward
		Message forward = new MimeMessage(session);

		// Fill in header
		forward.setSubject(subject);
		forward.setFrom(new InternetAddress(fromAddress));
		forward.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));

		// Create your new message part
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(body);

		// Create a multi-part to combine the parts
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Create and fill part for the forwarded content
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(message.getDataHandler());

		// Add part to multi part
		multipart.addBodyPart(messageBodyPart);

		// Associate multi-part with message
		forward.setContent(multipart);
		
		// send message
        Transport transport = session.getTransport("smtp");
        transport.connect(props.getProperty("mail.smtp.host"), props.getProperty("mail.user"), props.getProperty("mail.pwd"));
        transport.sendMessage(forward, forward.getAllRecipients());
        transport.close();
	}
	
	public static String getFromAddress(Message message) throws MessagingException {
		Address[] address;
		address = message.getFrom();
		
		if(address.length == 0) {
			throw new MessagingException("No address found.");
		} 
		
		return(((InternetAddress)address[0]).getAddress());
	}
	
	public static String getFromPersonal(Message message) throws MessagingException {
		Address[] address;
		address = message.getFrom();

		if(address.length == 0) {
			throw new MessagingException("No from address found.");
		} 
		
		return(((InternetAddress)address[0]).getPersonal());
	}
}
