package eu.qped.framework.qf;

import eu.qped.framework.FileInfo;

public class QfAssignment extends QfObjectBase {
	private String id;
	private String title;
	private FileInfo[] files;
	


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public FileInfo[] getFiles() {
		return files;
	}
	
	public void setFiles(FileInfo[] files) {
		this.files = files;
	}

}