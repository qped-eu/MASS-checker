package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Append extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(List<Expression> list) {
        Expression c1 = list.get(0);
        Expression c2 = list.get(1);

        return c1.evaluate(this).replace("'()", c2.evaluate(this));
    }

    @Override
    public String toString() {
        return "Append "  + " (" + super.getId() + ")";
    }
}
