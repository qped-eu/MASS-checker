package eu.qped.framework;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FileInfo {

	/**
	 * Simple filename on server, i.e., part of the URL.
	 */
	private String id;
	
	/**
	 * Simple filename of original file. 
	 */
	private String label;
	
	/**
	 * File extension, including a leading dot (".").
	 */
	private String extension;
	
	/**
	 * Path on the server, i.e., following the domain name in the URL.
	 */
	private String path;
	
	/**
	 * The file's mime type.
	 */
	private String mimetype;
	
	/**
	 * The URL for this file.
	 */
	private String url;
	
	public static FileInfo createForUrl(String url) throws MalformedURLException {		
		return createForUrl(url, URLConnection.guessContentTypeFromName(url));
	}
	
	public static FileInfo createForUrl(String url, String mimetype) throws MalformedURLException {		
		URL parsedUrl = new URL(url);
		
		String path = parsedUrl.getPath();

		
		FileInfo result = new FileInfo();
		result.url = url;
		result.mimetype = mimetype;
		result.id = FilenameUtils.getBaseName(path);
		result.label = FilenameUtils.getBaseName(path);
		result.extension = "." + FilenameUtils.getExtension(path);
		result.path = "";
		return result;
	}
	
	@JsonIgnore
	private File downloadedFile;
	
	@JsonIgnore
	private File unzippedDirectory;

	@JsonIgnore
	public File getDownloadedFile() {
		return downloadedFile;
	}

	@JsonIgnore
	protected void setDownloadedFile(File downloadedFile) {
		this.downloadedFile = downloadedFile;
	}

	@JsonIgnore
	public File getUnzippedDirectory() {
		return unzippedDirectory;
	}

	@JsonIgnore
	protected void setUnzippedDirectory(File unzippedDirectory) {
		this.unzippedDirectory = unzippedDirectory;
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
