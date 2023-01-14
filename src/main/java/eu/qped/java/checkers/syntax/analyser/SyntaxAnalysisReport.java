package eu.qped.java.checkers.syntax.analyser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;


/**
 * bildet ein Report für das Checken von Syntax ab.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyntaxAnalysisReport {

    private List<SyntaxError> syntaxErrors;
    private File path;
    private CompiledSourceType compiledSourceType;
    private boolean isCompilable;
    private String codeAsString;

    @Override
    public String toString() {
        return
                "Compilable: " + isCompilable + "\n" +
                        "path: " + path + "\n" +
                        "Compilation unit type: " + compiledSourceType + "\n" +
                        "errors count: " + syntaxErrors.size();
    }

}
