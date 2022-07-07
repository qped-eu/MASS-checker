package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

public class MethodKeywordConfig extends KeywordConfig {

    private String abstractModifier;
    private String synchronizedModifier;
    private String nativeModifier;
    private String defaultModifier;
    private String methodType;

    public MethodKeywordConfig() {
        abstractModifier = KeywordChoice.DONTCARE.toString();
        synchronizedModifier = KeywordChoice.DONTCARE.toString();
        nativeModifier = KeywordChoice.DONTCARE.toString();
        defaultModifier = KeywordChoice.DONTCARE.toString();
        methodType = "";
    }

    public String getAbstractModifier() {
        return abstractModifier;
    }

    public void setAbstractModifier(String abstractModifier) {
        this.abstractModifier = abstractModifier;
    }

    public String getDefaultModifier() {
        return defaultModifier;
    }

    public void setDefaultModifier(String defaultModifier) {
        this.defaultModifier = defaultModifier;
    }

    public String getSynchronizedModifier() {
        return synchronizedModifier;
    }

    public void setSynchronizedModifier(String synchronizedModifier) {
        this.synchronizedModifier = synchronizedModifier;
    }

    public String getNativeModifier() {
        return nativeModifier;
    }

    public void setNativeModifier(String nativeModifier) {
        this.nativeModifier = nativeModifier;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

}
