package eu.qped.java.utils.markdown;

public final class MarkdownFormatterUtility {
    public final static String NEW_Double_LINE = "\n\n";
    public final static String NEW_LINE = "\n";

    public static String asHeading2(String toFormat) {
        return NEW_Double_LINE + "## " + toFormat;
    }

    public static String asBold(String toFormat) {
        return NEW_Double_LINE + "**" + toFormat.trim() + "**";
    }

    public static String asJavaCodeBlock(String toFormat) {
        return NEW_Double_LINE + "```java" + NEW_LINE + toFormat + NEW_LINE + "```";
    }
}
