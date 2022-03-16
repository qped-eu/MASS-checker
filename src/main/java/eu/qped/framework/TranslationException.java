package eu.qped.framework;

public class TranslationException extends Exception {

	private String fromLanguage;
	private String toLanguage;

	public TranslationException(String fromLanguage, String toLanguage) {
		super("Translation failed from " + fromLanguage + " to " + toLanguage);
		this.fromLanguage = fromLanguage;
		this.toLanguage = toLanguage;
	}
	
	public String getFromLanguage() {
		return fromLanguage;
	}
	
	public String getToLanguage() {
		return toLanguage;
	}
	
}
