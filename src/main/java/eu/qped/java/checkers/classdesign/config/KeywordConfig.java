package eu.qped.java.checkers.classdesign.config;

import eu.qped.java.checkers.classdesign.enums.KeywordChoice;

public class KeywordConfig {

    private String publicModifier;
    private String protectedModifier;
    private String privateModifier;
    private String packagePrivateModifier;

    private String abstractModifier;
    private String staticModifier;
    private String finalModifier;

    private String name;

    public KeywordConfig() {
        publicModifier = KeywordChoice.IGNORE.toString();
        protectedModifier = KeywordChoice.IGNORE.toString();
        privateModifier = KeywordChoice.IGNORE.toString();
        packagePrivateModifier = KeywordChoice.IGNORE.toString();

        abstractModifier = KeywordChoice.IGNORE.toString();
        staticModifier = KeywordChoice.IGNORE.toString();
        finalModifier = KeywordChoice.IGNORE.toString();

        name = "";
    }

    public String getPublicModifier() {
        return publicModifier;
    }

    public void setPublicModifier(String publicModifier) {
        this.publicModifier = publicModifier;
    }

    public String getProtectedModifier() {
        return protectedModifier;
    }

    public void setProtectedModifier(String protectedModifier) {
        this.protectedModifier = protectedModifier;
    }

    public String getPrivateModifier() {
        return privateModifier;
    }

    public void setPrivateModifier(String privateModifier) {
        this.privateModifier = privateModifier;
    }

    public String getPackagePrivateModifier() {
        return packagePrivateModifier;
    }

    public void setPackagePrivateModifier(String packagePrivateModifier) {
        this.packagePrivateModifier = packagePrivateModifier;
    }

    public String getAbstractModifier() {
        return abstractModifier;
    }

    public void setAbstractModifier(String abstractModifier) {
        this.abstractModifier = abstractModifier;
    }

    public String getStaticModifier() {
        return staticModifier;
    }

    public void setStaticModifier(String staticModifier) {
        this.staticModifier = staticModifier;
    }

    public String getFinalModifier() {
        return finalModifier;
    }

    public void setFinalModifier(String finalModifier) {
        this.finalModifier = finalModifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
