package eu.qped.framework.feedback.template;

import eu.qped.framework.feedback.Type;
import eu.qped.java.checkers.solutionapproach.SolutionApproachChecker;
import eu.qped.java.checkers.solutionapproach.analyser.SolutionApproachAnalyser;
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
    public final static String KEY_TITLE_SEMANTIC = SolutionApproachChecker.class.getSimpleName();
    // Key Reference
    public final static String KEY_MORE_INFORMATION = "moreInformation";
    public final static String KEY_AT = "at";
    public final static String KEY_PAGE = "page";
    // Keys Related location
    public final static String KEY_IN = "in";
    public final static String KEY_METHOD = "method";
    public final static String KEY_BETWEEN_LINES = "betweenLines";
    public final static String KEY_AND = "and";
    public final static String KEY_LINE = "line";
    // Keys Feedback types
    public final static String KEY_CORRECTION = String.valueOf(Type.CORRECTION);
    public final static String KEY_IMPROVEMENT = String.valueOf(Type.IMPROVEMENT);
    // Hint
    public final static String KEY_CODE_EXAMPLE = "CODE_EXAMPLE";


    // EN
    private final static String EN_TITLE_SYNTAX = "Syntax error";
    private final static String EN_TITLE_STYLE = "Style issues";
    private final static String EN_TITLE_SEMANTIC = "Solution approach error";
    // EN Reference
    private final static String EN_MORE_INFORMATION = "for more information:";
    private final static String EN_AT = "at";
    private final static String EN_PAGE = "pages";
    // EN Related Location
    private final static String EN_IN = "in:";
    private final static String EN_METHOD = "method:";
    private final static String EN_BETWEEN_LINES = "between lines";
    private final static String EN_AND = "and";
    private final static String EN_LINE = "line";
    // EN Feedback types
    public final static String EN_MUST_BE_CORRECTED = "(must be corrected)";
    public final static String EN_TO_IMPROVEMENT = "(to improvement)";
    // Hint
    public final static String EN_CODE_EXAMPLE = "Code Example:";


    // DE
    private final static String DE_TITLE_SYNTAX = "Syntaktische Fehler";
    private final static String DE_TITLE_STYLE = "Stilproblem";
    private final static String DE_TITLE_SEMANTIC = "Lösungsansatz Fehler";
    // DE Reference
    private final static String DE_MORE_INFORMATION = "Mehr erfahren:";
    private final static String DE_AT = "in";
    private final static String DE_PAGE = "in Seiten";
    // DE Related Location
    private final static String DE_IN = "In:";
    private final static String DE_METHOD = "Methode:";
    private final static String DE_BETWEEN_LINES = "Zwischen Zeilen";
    private final static String DE_AND = "und";
    private final static String DE_LINE = "Zeile";
    // DE Feedback types
    public final static String DE_MUST_BE_CORRECTED = "(muss korrigiert werden)";
    public final static String DE_TO_IMPROVEMENT = "(zu Verbesserung)";
    // Hint
    public final static String DE_CODE_EXAMPLE = "Codebeispiel :";

    public Map<String, String> provide(String language) {
        Map<String, String> enTemplateKeyWord = new HashMap<>() {{
            put(KEY_TITLE_SYNTAX, EN_TITLE_SYNTAX);
            put(KEY_TITLE_STYLE, EN_TITLE_STYLE);
            put(KEY_TITLE_SEMANTIC, EN_TITLE_SEMANTIC);
            put(KEY_MORE_INFORMATION, EN_MORE_INFORMATION);
            put(KEY_AT, EN_AT);
            put(KEY_PAGE, EN_PAGE);
            put(KEY_IN, EN_IN);
            put(KEY_METHOD, EN_METHOD);
            put(KEY_BETWEEN_LINES, EN_BETWEEN_LINES);
            put(KEY_AND, EN_AND);
            put(KEY_LINE, EN_LINE);
            put(KEY_CORRECTION, EN_MUST_BE_CORRECTED);
            put(KEY_IMPROVEMENT, EN_TO_IMPROVEMENT);
            put(KEY_CODE_EXAMPLE, EN_CODE_EXAMPLE);
        }};
        Map<String, String> deTemplateKeyWord = new HashMap<>() {{
            put(KEY_TITLE_SYNTAX, DE_TITLE_SYNTAX);
            put(KEY_TITLE_STYLE, DE_TITLE_STYLE);
            put(KEY_TITLE_SEMANTIC, DE_TITLE_SEMANTIC);
            put(KEY_MORE_INFORMATION, DE_MORE_INFORMATION);
            put(KEY_AT, DE_AT);
            put(KEY_PAGE, DE_PAGE);
            put(KEY_IN, DE_IN);
            put(KEY_METHOD, DE_METHOD);
            put(KEY_BETWEEN_LINES, DE_BETWEEN_LINES);
            put(KEY_AND, DE_AND);
            put(KEY_LINE, DE_LINE);
            put(KEY_CORRECTION, DE_MUST_BE_CORRECTED);
            put(KEY_IMPROVEMENT, DE_TO_IMPROVEMENT);
            put(KEY_CODE_EXAMPLE, DE_CODE_EXAMPLE);
        }};
        Map<String, Map<String, String>> templateKeyWordMapByLanguage = new HashMap<>() {{
            put(SupportedLanguages.ENGLISH, enTemplateKeyWord);
            put(SupportedLanguages.GERMAN, deTemplateKeyWord);
        }};
        return templateKeyWordMapByLanguage.get(language);
    }


}
