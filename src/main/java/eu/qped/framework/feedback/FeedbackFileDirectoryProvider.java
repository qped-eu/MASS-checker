package eu.qped.framework.feedback;

import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.checkers.syntax.feedback.template.TemplateBuilder;
import org.jetbrains.annotations.NotNull;

public class FeedbackFileDirectoryProvider {

    private final static String STYLE_DIR_PATH = "src/main/resources/style/";

    private final static String SYNTAX_DIR_PATH = "src/main/resources/syntax/";

    private final static String SEMANTIC_DIR_PATH = "src/main/resources/semantic/";



    public static String provide(@NotNull Class<?> aClass) {
        if (aClass.equals(StyleChecker.class)) {
            return STYLE_DIR_PATH;
        }
        if (aClass.equals(SemanticChecker.class)) {
            return SEMANTIC_DIR_PATH;
        }
        if (aClass.equals(SyntaxChecker.class)) {
            return SYNTAX_DIR_PATH;
        } else return "";
    }

}
