package eu.qped.framework.feedback.defaultfeedback;

import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import org.jetbrains.annotations.NotNull;

public class DefaultFeedbackDirectoryProvider {

    private final static String STYLE_DIR_PATH = "src/main/resources/style/";

    private final static String SYNTAX_DIR_PATH = "src/main/resources/syntax/";

    private final static String SEMANTIC_DIR_PATH = "src/main/resources/semantic/";



    /**
     * @param aClass is the checker therefore feedbacks needed.
     * @return the path of a file basically json that contains all the data of the feedbacks pro checker.
     */
    public static String provideDefaultFeedbackDirectory(@NotNull Class<?> aClass) {
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
