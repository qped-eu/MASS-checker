package eu.qped.umr.model;

public abstract class Feedback {

    String body;


    public Feedback (String body){
        this.body = body;

    }

    public String getBody() {
        return body;
    }

    public void setBody(String head) {
        this.body = head;
    }

}
