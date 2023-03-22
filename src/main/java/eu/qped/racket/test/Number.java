package eu.qped.racket.test;

public class Number extends Expression {
    private final float value;

    public Number(float value) {
        this.value = value;
    }

    @Override
    public String evaluate(Expression e) {
        return Float.toString(value);
    }

    @Override
    public String toString() {
        return "Number " + value + " (" + super.getId() + ")";
    }
}
