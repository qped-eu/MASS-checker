package eu.qped.java.checkers.syntax;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyntaxError {

    @Deprecated(forRemoval = true)
    private Map<String, String> additionalProperties;

    private String errorCode;
    private String errorMessage;

    @Deprecated(forRemoval = true)
    private String errorSourceCode;

    private String errorTrigger;
    private String fileName;

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
                        this.getErrorMessage().equals(that.getErrorMessage()) &&
                        this.getStartPos() == that.getStartPos() &&
                        this.getErrorTrigger().equals(that.getErrorTrigger()) &&
                        this.getAdditionalProperties().equals(that.getAdditionalProperties());
    }
}
