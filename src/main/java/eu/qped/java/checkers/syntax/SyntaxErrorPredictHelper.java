package eu.qped.java.checkers.syntax;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(forRemoval = true)
public class SyntaxErrorPredictHelper {


    private String errorCode;
    private String errorTrigger;
    private String errorMsg;


    public String getErrorKind() {
        String errorKind = "";
        switch (errorCode) {
            case "compiler.err.illegal.start.of.expr": {
                boolean hasMod = hasModifier(errorTrigger);
                if (!isAMethod(errorTrigger) && hasMod) {
                    errorKind = "variable";
                } else if (isAMethod(errorTrigger) && !errorTrigger.contains("(")) {
                    errorKind = "braces || strLit";
                } else if (isAMethod(errorTrigger)) {
                    errorKind = "method";
                }
            }
            case "compiler.err.illegal.start.of.type": {

            }
            case "compiler.err.expected":
            case "compiler.err.expected3":
            case "compiler.err.expected1":
            case "compiler.err.expected2": {
                if (errorMsg.equals("<identifier> expected")) {
                    if (errorTrigger.contains("(") && errorTrigger.contains(")")) {
                        errorKind = "method";
                    } else if (errorTrigger.contains("=")) {
                        errorKind = "variable";
                    }
                }
            }
        }
        return errorKind;
    }

    public boolean isAMethod(String input) {
        for (int i = input.length() - 1; i >= 0; i--) {
            if (input.charAt(i) == ' ' || input.charAt(i) != ')') {
                continue;
            } else return input.charAt(i) == ')' && (input.contains("return") || input.contains("void"));
        }
        return false;
    }

    public boolean hasBraces(String input) {
        return input.contains(")");
    }


    private boolean hasModifier(String input) {
        return input.contains("public") || input.contains("private") || input.contains("protected");
    }

    public boolean hasEqualNumberOfBraces(String input) {
        int openCounter = 0;
        int closedCounter = 0;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '{') {
                openCounter++;
            } else if (input.charAt(i) == '}') {
                closedCounter++;
            }
        }

        return openCounter == closedCounter;
    }

}
