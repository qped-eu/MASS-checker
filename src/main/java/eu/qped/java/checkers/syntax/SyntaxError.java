package eu.qped.java.checkers.syntax;


import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyntaxError {
    private String errorCode;
    private String errorMsg;
    private long line;
    private String errorTrigger;

    private long startPos;
    private long endPos;
    private Map<String, String> additionalProperties;

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
