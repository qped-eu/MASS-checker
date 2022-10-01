package eu.qped.java.utils.markdown;

import net.steppschuh.markdowngenerator.text.emphasis.BoldText;

public class FormattedHeader {

    private String header;

    public FormattedHeader(String header) {
        this.header = header;
    }


    public String getFormattedHeader() {
        return String.valueOf(new BoldText(this.header));
    }

    public static void main(String[] args) {

        String syntax;
        String style;
        String semantic;


    }

}
