package eu.qped.java.checkers.syntax.feedback.template;

import eu.qped.java.checkers.semantics.SemanticChecker;
import eu.qped.java.checkers.style.StyleChecker;
import eu.qped.java.checkers.syntax.SyntaxChecker;
import eu.qped.java.utils.SupportedLanguages;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor
public class TemplateTextProvider {

    // Keys
    public final static String KEY_TITLE_SYNTAX = SyntaxChecker.class.getSimpleName();
    public final static String KEY_TITLE_STYLE = StyleChecker.class.getSimpleName();
    public final static String KEY_TITLE_SEMANTIC = SemanticChecker.class.getSimpleName();
    public final static String KEY_MORE_INFORMATION = "moreInformation";
    public final static String KEY_AT = "at";
    public final static String KEY_PAGE = "page";

    // EN
    private final static String EN_TITLE_SYNTAX = "Syntax error";
    private final static String EN_TITLE_STYLE = "Style issues";
    private final static String EN_TITLE_SEMANTIC = "Solution approach error";
    private final static String EN_MORE_INFORMATION = "for more information:";
    private final static String EN_AT = "at";
    private final static String EN_PAGE = "pages";

    // DE
    private final static String DE_TITLE_SYNTAX = "Syntaktische Fehler";
    private final static String DE_TITLE_STYLE = "Stilproblem";
    private final static String DE_TITLE_SEMANTIC = "Lösungsansatz Fehler";
    private final static String DE_MORE_INFORMATION = "Mehr erfahren:";
    private final static String DE_AT = "in";
    private final static String DE_PAGE = "in Seiten";

    public Map<String, String> provide(String language) {
        Map<String, String> enTemplateKeyWord = new HashMap<>() {{
            put(KEY_TITLE_SYNTAX, EN_TITLE_SYNTAX);
            put(KEY_TITLE_STYLE, EN_TITLE_STYLE);
            put(KEY_TITLE_SEMANTIC, EN_TITLE_SEMANTIC);
            put(KEY_MORE_INFORMATION, EN_MORE_INFORMATION);
            put(KEY_AT, EN_AT);
            put(KEY_PAGE, EN_PAGE);
        }};

        Map<String, String> deTemplateKeyWord = new HashMap<>() {{
            put(KEY_TITLE_SYNTAX, DE_TITLE_SYNTAX);
            put(KEY_TITLE_STYLE, DE_TITLE_STYLE);
            put(KEY_TITLE_SEMANTIC, DE_TITLE_SEMANTIC);
            put(KEY_MORE_INFORMATION, DE_MORE_INFORMATION);
            put(KEY_AT, DE_AT);
            put(KEY_PAGE, DE_PAGE);
        }};
        Map<String, Map<String, String>> templateKeyWordMapByLanguage = new HashMap<>() {{
            put(SupportedLanguages.ENGLISH, enTemplateKeyWord);
            put(SupportedLanguages.GERMAN, deTemplateKeyWord);
        }};

        return templateKeyWordMapByLanguage.get(language);
    }


}
