package eu.qped.java.checkers.style;

import eu.qped.framework.Feedback;

public class StyleFeedback extends Feedback {

    private String desc;
    private String line;
    private String example;

    public StyleFeedback( String desc,String body , String example , String line) {
        super(body);
        this.example = example;
        this.line = line;
        this.desc = desc;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
