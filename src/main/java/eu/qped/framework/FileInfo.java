package eu.qped.framework;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileInfo {

	private String id;
	private String label;
	private String extension;
	private String path;
	private String mimetype;
	private String url;
	
	@JsonIgnore
	private File submittedFile;
	
	@JsonIgnore
	private File unzipped;

	@JsonIgnore
	public File getSubmittedFile() {
		return submittedFile;
	}

	@JsonIgnore
	protected void setSubmittedFile(File submittedFile) {
		this.submittedFile = submittedFile;
	}

	@JsonIgnore
	public File getUnzipped() {
		return unzipped;
	}

	@JsonIgnore
	protected void setUnzipped(File unzipped) {
		this.unzipped = unzipped;
	}

	public String getId() {
		return id;
	}
	
	protected void setId(String id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	
	protected void setLabel(String label) {
		this.label = label;
	}
	
	public String getExtension() {
		return extension;
	}
	
	protected void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getPath() {
		return path;
	}
	
	protected void setPath(String path) {
		this.path = path;
	}
	
	public String getMimetype() {
		return mimetype;
	}
	
	protected void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	
	public String getUrl() {
		return url;
	}
	
	protected void setUrl(String url) {
		this.url = url;
	}
	
}
