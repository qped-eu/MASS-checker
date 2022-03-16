package eu.qped.framework.qf;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class QfObjectBase {

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	public QfObjectBase() {
		super();
	}

	@JsonAnyGetter
	final public Map<String, Object> getAdditionalProperties() {
	    return additionalProperties;
	}

	final public boolean hasProperty(String property) {
		return additionalProperties.containsKey(property);
	}

	@JsonAnySetter
	final public void setAdditionalProperty(String property, Object value) {
		additionalProperties.put(property, value);
	}

	@SuppressWarnings("unchecked")
	@JsonAnyGetter
	final public <T> T getAdditionalProperty(String property) {
	    return (T) additionalProperties.get(property);
	}

}