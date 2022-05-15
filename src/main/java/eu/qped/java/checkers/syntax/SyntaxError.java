package eu.qped.java.checkers.syntax;


import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyntaxError {

    private Map<String, String> additionalProperties;

    private String errorCode;
    private String errorMsg;
    private String errorTrigger;
    private Kind kind;
    private JavaFileObject source;

    private long line;
    private long startPos;
    private long endPos;
    private long columnNumber;

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof SyntaxError)) return false;
        SyntaxError that = (SyntaxError) o;
        return
                this.getErrorCode().equalsIgnoreCase(that.getErrorCode()) &&
                        this.getErrorMsg().equals(that.getErrorMsg()) &&
                        this.getStartPos() == that.getStartPos() &&
                        this.getErrorTrigger().equals(that.getErrorTrigger()) &&
                        this.getAdditionalProperties().equals(that.getAdditionalProperties());
    }
}
