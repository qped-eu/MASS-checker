package eu.qped.java.checkers.classdesign.infos;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {

    //TODO check how to implement
    private String fullyQualifiedName;
    private String classKeywords;
    private List<String> inheritsFrom;
    private List<String> fieldKeywords;
    private List<String> methodKeywords;

    public ClassInfo() {
        fullyQualifiedName = "";
        classKeywords = "";
        inheritsFrom = new ArrayList<>();
        fieldKeywords = new ArrayList<>();
        methodKeywords = new ArrayList<>();
    }

    public String getClassKeywords() {
        return classKeywords;
    }

    public void setClassKeywords(String classKeywords) {
        this.classKeywords = classKeywords;
    }

    public List<String> getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(ArrayList<String> inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public List<String> getFieldKeywords() {
        return fieldKeywords;
    }

    public void setFieldKeywords(ArrayList<String> fieldKeywords) {
        this.fieldKeywords = fieldKeywords;
    }

    public List<String> getMethodKeywords() {
        return methodKeywords;
    }

    public void setMethodKeywords(ArrayList<String> methodKeywords) {
        this.methodKeywords = methodKeywords;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }
}
