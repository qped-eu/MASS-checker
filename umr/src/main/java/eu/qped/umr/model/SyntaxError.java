package eu.qped.umr.model;


import java.util.Map;

public class SyntaxError {
    private final String errorCode;
    private final String errorMsg;
    private final long line;
    private final String errorTrigger;

    private final long startPos;
    private final long endPos;
    private final Map<String , String> additionalProperties;


    public SyntaxError(String errorCode, String errorMsg, long line, String errorTrigger, Map<String , String> additionalProperties , long startPos , long endPos) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.line = line;
        this.errorTrigger = errorTrigger;
        this.startPos = startPos;
        this.endPos = endPos;
        this.additionalProperties = additionalProperties;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public long getLine() {
        return line;
    }

    public String getErrorTrigger() {
        return errorTrigger;
    }



    public long getStartPos() {
        return startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }
}
