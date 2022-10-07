package eu.qped.java.utils.markdown;

import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.rule.HorizontalRule;
import net.steppschuh.markdowngenerator.text.Text;
import net.steppschuh.markdowngenerator.text.code.Code;
import net.steppschuh.markdowngenerator.text.code.CodeBlock;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.heading.Heading;

/**
 * Utility class to format text in markdown
 *
 * @author Omar Aji
 * @since 02.10.2022
 */
public final class MarkdownFormatterUtility {

    public final static String NEW_Double_LINE = "\n\n";
    public final static String NEW_LINE = "\n";
    public final static String SPACE = " ";
    public final static String DOT = ".";
    public final static String CODE_JAVA = "Java";
    public final static String HORIZONTAL_RULE = new HorizontalRule().toString();



    /**
     * @param toFormat string to format
     * @return the markdown formatted heading 4 string
     */
    public static String asHeading4(String toFormat) {
        if (toFormat == null || toFormat.equals("")) return "";
        return new Heading(toFormat.trim(), 4).toString();
    }


    /**
     * @param toFormat string to format
     * @return the markdown formatted bold string
     */
    public static String asBold(String toFormat) {
        if (toFormat == null || toFormat.equals("")) return "";
        return new BoldText(toFormat.trim()).toString();
    }

    /**
     * Formatter for italic.
     *
     * @param toFormat the string to format
     * @return the md italic formatted String
     */
    public static String asItalic(String toFormat) {
        if (toFormat == null || toFormat.equals("")) return "";
        return new BoldText(toFormat.trim()).toString();
    }

    /**
     * @param toFormat string to format
     * @param language code language
     * @return the markdown formatted code Block
     */
    public static String asCodeBlock(String toFormat, String language) {
        if (language == null) language = CODE_JAVA;
        if (toFormat == null || toFormat.equals("")) return "";
        return new CodeBlock(toFormat.trim(), language).toString();
    }

    /**
     * @param toFormat string to format
     * @return the markdown formatted code line
     */
    public static String asCodeLine(String toFormat) {
        if (toFormat == null || toFormat.equals("")) return "";
        return new Code(toFormat.trim()).toString();
    }


    /**
     * @param toFormat string to format
     * @param url      string contains a link
     * @return the markdown formatted link
     */
    public static String asLink(String toFormat, String url) {
        if (toFormat == null) toFormat = "";
        if (url != null && !url.equals("")) {
            return new Link(toFormat.trim(), url).toString();
        } else {
            return new Text(toFormat.trim()).toString();
        }
    }


}
