package eu.qped.java.utils.markdown;

public final class MarkdownFormatterUtility {

    public final static String NEW_Double_LINE = "\n\n";
    public final static String NEW_LINE = "\n";

    public static String asHeading2(String toFormat) {
        return NEW_Double_LINE + "## " + toFormat;
    }

    public static String asHeading3(String toFormat) {
        return NEW_Double_LINE + "### " + toFormat;
    }

    public static String asBold(String toFormat) {
        return NEW_Double_LINE + "**" + toFormat.trim() + "**";
    }

    public static String asJavaCodeBlock(String toFormat) {
        return NEW_Double_LINE + "```java" + NEW_LINE + toFormat + NEW_LINE + "```";
    }

    /**
     * Formatter for monospacing.
     *
     * @param toFormat the string to format
     * @param multiline whether to format as single or multiline
     * @param type when using, multiline set the type, e.g. ```java or ```txt
     * @return the md formatted String
     */
    public static String asMonospace(String toFormat, boolean multiline, String type) {
        if (!multiline) {
            return "`" + toFormat + "`";
        } else {
            return NEW_Double_LINE + "```" + type + NEW_LINE + toFormat + NEW_LINE + "```";
        }
    }

    /**
     * Formatter for italic.
     *
     * @param toFormat the string to format
     * @return the md italic formatted String
     */
    public static String asItalic(String toFormat) {
       return "_" + toFormat + "_";
    }
}
