package eu.qped.java.checkers.syntax;

import eu.qped.framework.Feedback;

public class SyntaxFeedback extends Feedback {

    private  String head;
    private  String example;



    public SyntaxFeedback (String head , String body , String example){
        super(body);
        this.head = head;
        this.example =example;
    }

    public String getHead() {
        return head;
    }

    public String getExample() {
        return example;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
