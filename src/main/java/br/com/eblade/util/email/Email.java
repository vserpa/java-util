package br.com.eblade.util.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Email implements Serializable {
	
	private static final long serialVersionUID = -3030794180812789415L;

	private String fromAddress;

	private String fromPersonal;

	private List<String> recipients;

	private String subject;

	private String body;

	private List<String> cc;

	private List<String> bcc;

	private String header;

	private String signature;

	private List<Attachment> attachments;

	private List<Attachment> contentIds;
	
	private String contentType;

	public Email() {
		attachments = new ArrayList<Attachment>();
		contentIds = new ArrayList<Attachment>();
		recipients = new ArrayList<String>();
		cc = new ArrayList<String>();
		bcc = new ArrayList<String>();
	}

	public Email(List<String> recipients, String subject, String body) {
		this();
		this.recipients = recipients;
		this.subject = subject;
		this.body = body;
	}

	public Email(String fromAddress, String fromPersonal, List<String> recipients,
			List<String> cc, List<String> bcc, String subject, String body,
			String signature, String contentType) {

		this(recipients, subject, body);
		this.cc = cc;
		this.bcc = bcc;
		this.signature = signature;
		this.contentType = contentType;
		this.fromAddress = fromAddress;
		this.fromPersonal = fromPersonal;
	}
	
	public void addAttachment(Attachment attachment) {
		attachments.add(attachment);
	}
	
	public void addContentId(Attachment attachment) {
		contentIds.add(attachment);
	}
	
	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getFromPersonal() {
		return fromPersonal;
	}

	public void setFromPersonal(String fromPersonal) {
		this.fromPersonal = fromPersonal;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	
	public void addRecipient(String recipient) {
		recipients.add(recipient);
	}

	public void addCarbonCopy(String carbonCopy) {
		cc.add(carbonCopy);
	}

	public void addBlindCarbonCopy(String blindCarbonCopy) {
		bcc.add(blindCarbonCopy);
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Attachment> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<Attachment> contentIds) {
		this.contentIds = contentIds;
	}
	
	@Override
	public String toString() {
		return(new ToStringBuilder(this)
		.append("fromAddress", fromAddress)
		.append("fromPersonal", fromPersonal)
		.append("recipients", recipients)
		.append("subject", subject)
		.append("cc", cc)
		.append("bcc", bcc)
		.append("header", header)
		.append("signature", signature)
		.append("attachments", attachments)
		.append("contentIds", contentIds)
		.append("contentType", contentType)
		.toString());
	}

}
