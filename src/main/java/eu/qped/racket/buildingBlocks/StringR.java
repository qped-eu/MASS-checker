package eu.qped.racket.buildingBlocks;

public class StringR extends Expression {

    private final String value;

    public StringR(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Expression e) {
        return value;
    }

    @Override
    public String toString() {
        return "StringR " + value + " (" + super.getId() + ")";
    }
}
