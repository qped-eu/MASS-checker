package eu.qped.java.checkers.syntax.analyser;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyntaxError {

    private String errorCode;
    private String errorMessage;

    private String errorTrigger;
    private String fileName;

    private long line;
    private long startPos;
    private long endPos;
    private long columnNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyntaxError)) return false;
        SyntaxError that = (SyntaxError) o;
        return getLine() == that.getLine()
                && getStartPos() == that.getStartPos()
                && getEndPos() == that.getEndPos()
                && getColumnNumber() == that.getColumnNumber()
                && Objects.equals(getErrorCode(), that.getErrorCode())
                && Objects.equals(getErrorMessage(), that.getErrorMessage())
                && Objects.equals(getErrorTrigger(), that.getErrorTrigger())
                && Objects.equals(getFileName(), that.getFileName());
    }

}
