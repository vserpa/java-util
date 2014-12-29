package br.com.eblade.util.email;

import java.io.File;
import java.io.InputStream;

public class Attachment {

	public enum Type {File, InputStream};
	
	private File f;
	private String name;
	private InputStream is;
	private Type type;
	private String contentType;
	
	public Attachment(File file) {
		this.f = file;
		this.is = null;
		this.type = Type.File;
		this.name = file.getName();
	}

	public Attachment(File file, String fileName) {
		this.f = file;
		this.is = null;
		this.type = Type.File;
		this.name = fileName;
	}

	public Attachment(InputStream is, String fileName) {
		this.f = null;
		this.is = is;
		this.type = Type.InputStream;
		this.name = fileName;
	}

	public File getF() {
		return f;
	}

	public void setF(File f) {
		this.f = f;
	}

	public String getName() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public File getAttachmentAsFile() {
		return(f);
	}

	public InputStream getAttachmentAsInputStream() {
		return(is);
	}
}
