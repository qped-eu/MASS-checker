package eu.qped.umr.qf;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class QFMainSettings {

    private String syntaxLevel;
    private String preferredLanguage;
    private String styleNeeded;
    private String semanticNeeded;
    
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }



	public boolean hasProperty(String property) {
		return additionalProperties.containsKey(property);
	}



	@JsonAnySetter
	public void setAdditionalProperty(String property, String value){
		additionalProperties.put(property, value);
	}


    public String getSyntaxLevel() {
        return syntaxLevel;
    }

    public void setSyntaxLevel(String syntaxLevel) {
        this.syntaxLevel = syntaxLevel;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getStyleNeeded() {
        return styleNeeded;
    }

    public void setStyleNeeded(String styleNeeded) {
        this.styleNeeded = styleNeeded;
    }

    public String getSemanticNeeded() {
        return semanticNeeded;
    }

    public void setSemanticNeeded(String semanticNeeded) {
        this.semanticNeeded = semanticNeeded;
    }
}
