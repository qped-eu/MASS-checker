package eu.qped.racket.functions.booleans;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class BooleanEQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        for (Expression e : list) {
            if (!(Boolean) e.evaluate(this))
                return Boolean.toString(false);
        }
        return Boolean.toString(true);
    }

    @Override
    public String toString() {
        return "BooleanEQ" + "(" + super.getId() + ")";
    }
}
