package eu.qped.java.checkers.design.infos;

import java.util.ArrayList;

public class ClassInfo {

    private String classTypeName;
    private ArrayList<String> inheritsFrom;
    private ArrayList<String> fieldKeywords;
    private ArrayList<String> methodKeywords;

    public ClassInfo() {
        classTypeName = "";
        inheritsFrom = new ArrayList<>();
        fieldKeywords = new ArrayList<>();
        methodKeywords = new ArrayList<>();
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }

    public ArrayList<String> getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(ArrayList<String> inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public ArrayList<String> getFieldKeywords() {
        return fieldKeywords;
    }

    public void setFieldKeywords(ArrayList<String> fieldKeywords) {
        this.fieldKeywords = fieldKeywords;
    }

    public ArrayList<String> getMethodKeywords() {
        return methodKeywords;
    }

    public void setMethodKeywords(ArrayList<String> methodKeywords) {
        this.methodKeywords = methodKeywords;
    }
}
