package eu.qped.racket.functions.booleans;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class BooleanQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        for (Expression e : list) {
            if (e.evaluate(this).equals("true") || e.evaluate(this).equals("false"))
                return Boolean.toString(true);
        }
        return Boolean.toString(false);
    }

    @Override
    public String toString() {
        return "BooleanQ" + "(" + super.getId() + ")";
    }
}
