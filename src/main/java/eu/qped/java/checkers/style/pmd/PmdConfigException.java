package eu.qped.java.checkers.style.pmd;

public class PmdConfigException extends Exception {

	private static final long serialVersionUID = 6201398871053777341L;
	
	private String xmlPath;
	private String ruleName;
	private String propName;

	public PmdConfigException(String xmlPath, String ruleName, String propName) {
		super((xmlPath == null ? "xmlPath must not be null"
				: (ruleName == null ? "ruleName must not be null"
						: (propName == null ? "propertyName must not be null" : "Element not found")))
				+ " (xmlPath: " + xmlPath + ", ruleName: " + ruleName + ", propName: " + propName + ")");
		this.xmlPath = xmlPath;
		this.ruleName = ruleName;
		this.propName = propName;

	}

	public PmdConfigException(String message, String xmlPath, String ruleName, String propName) {
		super(message + " (xmlPath: " + xmlPath + ", ruleName: " + ruleName + ", propName: " + propName + ")");
		this.xmlPath = xmlPath;
		this.ruleName = ruleName;
		this.propName = propName;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public String getRuleName() {
		return ruleName;
	}

	public String getPropName() {
		return propName;
	}

}
