package eu.qped.racket.buildingBlocks;

public class Number extends Expression {
    private final float value;

    public Number(float value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Expression e) {
        return value;
    }

    @Override
    public String toString() {
        return "Number " + value + " (" + super.getId() + ")";
    }
}
