package eu.qped.racket.buildingBlocks;

public class Boolean extends Expression {
    private final java.lang.Boolean value;

    public Boolean(java.lang.Boolean value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Expression e) {
        return value;
    }

    @Override
    public String toString() {
        return "Boolean " + value + " (" + super.getId() + ")";
    }
}
