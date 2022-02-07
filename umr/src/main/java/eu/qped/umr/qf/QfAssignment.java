package eu.qped.umr.qf;

public class QfAssignment extends QfObjectBase {
	private String id;
	private String title;
	private String[] files;
	
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



	public String[] getFiles() {
		return files;
	}



	public void setFiles(String[] files) {
		this.files = files;
	}
	

	
}