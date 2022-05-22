package eu.qped.java.feedback.syntax;

import eu.qped.java.utils.markdown.MarkdownFormatterUtility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class SyntaxFeedbackSolutionExamples {


    public Map<String, String> getSolutionExamplesByErrorCode() {
        return new HashMap<>() {{
            put("compiler.err.already.defined"
                    , ""
                            + "// old code" + MarkdownFormatterUtility.NEW_LINE
                            + "int oddNumber = 7;" + MarkdownFormatterUtility.NEW_LINE
                            + "int oddNumber = 7;" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code" + MarkdownFormatterUtility.NEW_LINE
                            + "int oddNumber = 7;" + MarkdownFormatterUtility.NEW_LINE
                            + "int newOddNumber = 7;" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.not.stmt"
                    , ""
                            + "// old code. We write expression in the if statement instead a statement." + MarkdownFormatterUtility.NEW_LINE
                            + "if (i == 1) {" + MarkdownFormatterUtility.NEW_LINE
                            + "     \"one\";" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code. \"one\" is a expression and not a statement." + MarkdownFormatterUtility.NEW_LINE
                            + "if (i == 1) {" + MarkdownFormatterUtility.NEW_LINE
                            + "     System.out.println(\"one\");" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.unclosed.str.lit"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "String firstName = \"myFirstName;" + MarkdownFormatterUtility.NEW_LINE
                            + "String name = \"myFirstName " + MarkdownFormatterUtility.NEW_LINE
                            + "     myLastName\";" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code. " + MarkdownFormatterUtility.NEW_LINE
                            + "String firstName = \"myFirstName\";" + MarkdownFormatterUtility.NEW_LINE
                            + "String name = \"myFirstName\" " + MarkdownFormatterUtility.NEW_LINE
                            + "     + \"myLastName\";"
            );
            put("compiler.err.else.without.if"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "else {" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code." + MarkdownFormatterUtility.NEW_LINE
                            + "if(true) {" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
                            + "else {" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.missing.ret.stmt"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "int myMethod(){" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code." + MarkdownFormatterUtility.NEW_LINE
                            + "int myMethod(){" + MarkdownFormatterUtility.NEW_LINE
                            + "    return 0;" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.unreachable.stmt"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "int myMethod(){" + MarkdownFormatterUtility.NEW_LINE
                            + "    return 0;" + MarkdownFormatterUtility.NEW_LINE
                            + "    int a = 0;" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code." + MarkdownFormatterUtility.NEW_LINE
                            + "int myMethod(){" + MarkdownFormatterUtility.NEW_LINE
                            + "    int a = 0;" + MarkdownFormatterUtility.NEW_LINE
                            + "    return a;" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.var.might.not.have.been.initialized"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "int a ;" + MarkdownFormatterUtility.NEW_LINE
                            + "int b = a;" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code." + MarkdownFormatterUtility.NEW_LINE
                            + "int a = 0;" + MarkdownFormatterUtility.NEW_LINE
                            + "int b = a;" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.cant.resolve.location"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "int a = b;" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code." + MarkdownFormatterUtility.NEW_LINE
                            + "int b = 0;" + MarkdownFormatterUtility.NEW_LINE
                            + "int a = b;" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.repeated.modifier"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "public public int i;" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code." + MarkdownFormatterUtility.NEW_LINE
                            + "public int i;" + MarkdownFormatterUtility.NEW_LINE
            );
            put("compiler.err.illegal.combination.of.modifiers"
                    , ""
                            + "// old code." + MarkdownFormatterUtility.NEW_LINE
                            + "public public int i;" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code." + MarkdownFormatterUtility.NEW_LINE
                            + "public static int i;" + MarkdownFormatterUtility.NEW_LINE
            );
        }};
    }

    public Map<String, String> getSolutionExamplesByErrorMessage() {
        return new HashMap<>() {{
            put("';' expected"
                    , ""
                            + "// old code" + MarkdownFormatterUtility.NEW_LINE
                            + "int oddNumber = 7" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code" + MarkdownFormatterUtility.NEW_LINE
                            + "int oddNumber = 7;" + MarkdownFormatterUtility.NEW_LINE
            );
            put("'(' expected"
                    , ""
                            + "// old code" + MarkdownFormatterUtility.NEW_LINE
                            + "if true) {" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code" + MarkdownFormatterUtility.NEW_LINE
                            + "if (true) {" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
            );
            put("')' expected"
                    , ""
                            + "// old code" + MarkdownFormatterUtility.NEW_LINE
                            + "if (true {" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code" + MarkdownFormatterUtility.NEW_LINE
                            + "if (true) {" + MarkdownFormatterUtility.NEW_LINE
                            + "}" + MarkdownFormatterUtility.NEW_LINE
            );
            put("<identifier> expected"
                    , ""
                            + "// old code" + MarkdownFormatterUtility.NEW_LINE
                            + "int  = 7; // no name" + MarkdownFormatterUtility.NEW_LINE
                            + "// new code" + MarkdownFormatterUtility.NEW_LINE
                            + "int i = 7;" + MarkdownFormatterUtility.NEW_LINE
            );
        }};
    }
}
