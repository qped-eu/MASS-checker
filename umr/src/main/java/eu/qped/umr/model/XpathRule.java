package eu.qped.umr.model;


import java.util.Map;

public class XpathRule {

    private final String name;
    private final String message;
    private final String pmdClass;
    private final String description;
    private final Map <String , String> properties;

    public XpathRule (String name, String message , String pmdClass , String description , Map<String, String> properties){
        this.name = name;
        this.message = message;
        this.pmdClass = pmdClass;
        this.description = description;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getPmdClass() {
        return pmdClass;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
