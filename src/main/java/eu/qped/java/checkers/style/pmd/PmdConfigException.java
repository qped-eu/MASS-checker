package eu.qped.java.checkers.style.pmd;

public class PmdConfigException extends Exception {

	private static final long serialVersionUID = 6201398871053777341L;
	
	private String ruleName;
	private String propName;

	public PmdConfigException(String ruleName, String propName) {
		super((ruleName == null ? "ruleName must not be null"
						: (propName == null ? "propertyName must not be null" : "Element not found"))
				+ " (ruleName: " + ruleName + ", propName: " + propName + ")");
		this.ruleName = ruleName;
		this.propName = propName;

	}

	public PmdConfigException(String message, String ruleName, String propName) {
		super(message + " (ruleName: " + ruleName + ", propName: " + propName + ")");
		this.ruleName = ruleName;
		this.propName = propName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public String getPropName() {
		return propName;
	}

}
