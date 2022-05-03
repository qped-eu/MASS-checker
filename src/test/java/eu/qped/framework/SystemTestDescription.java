package eu.qped.framework;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SystemTestDescription {

	private String title;
	
	private String description;
	
	private String author;
	
	private boolean disabled;
	
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getTitle() {
		return title;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	protected void setAuthor(String author) {
		this.author = author;
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonAnySetter
	public void addAdditionslProperty(String propertyKey, Object value) {
		this.additionalProperties.put(propertyKey, value);
	}
	
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
    
    @JsonIgnore
    public Object getAdditionalProperty(String propertyKey) {
    	return additionalProperties.get(propertyKey);
    }
	
}
