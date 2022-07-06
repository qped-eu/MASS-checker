package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

public class MethodKeywordConfig extends KeywordConfig {

    private String synchronizedModifier;
    private String nativeModifier;
    private String defaultModifier;
    private String methodType;

    public MethodKeywordConfig() {
        synchronizedModifier = KeywordChoice.IGNORE.toString();
        nativeModifier = KeywordChoice.IGNORE.toString();
        defaultModifier = KeywordChoice.IGNORE.toString();
        methodType = "";
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
