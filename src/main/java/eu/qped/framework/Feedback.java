package eu.qped.framework;

public abstract class Feedback {

    String body;


    public Feedback (String body){
        this.body = body;

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
