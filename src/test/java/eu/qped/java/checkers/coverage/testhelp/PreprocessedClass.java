package eu.qped.java.checkers.coverage.testhelp;

import eu.qped.java.checkers.coverage.CovInformation;

public class PreprocessedClass implements CovInformation {
    private final String className;
    private final String content;
    private final byte[] byteCode;


    protected PreprocessedClass(String className, String content, byte[] byteCode) {
        this.className = className;
        this.content = content;
        this.byteCode = byteCode;
    }

    @Override
    public String simpleClassName() {
        return className.substring(className.lastIndexOf(".") + 1);
    }

    @Override
    public String className() {
        return className;
    }

    @Override
    public byte[] byteCode() {
        return byteCode;
    }

    @Override
    public String content() {
        return content;
    }
}
