package eu.qped.racket.test;

import java.util.LinkedList;

public class RList extends Expression {

    private final LinkedList value;

    public RList(LinkedList value) {
        this.value = value;
    }

    @Override
    public String evaluate(Expression e) {
        return (String) value.stream().map(x -> x.toString()).
                reduce((x,y) -> (x + " , " + y)).
                orElse("");
    }

    @Override
    public String toString() {
        return "RList " + value + " (" + super.getId() + ")";
    }
}
