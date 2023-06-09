package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Append extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        Expression c1 = list.get(0);
        Expression c2 = list.get(1);

        return c1.evaluate(this).toString().replace("'()",(String) c2.evaluate(this));
    }

    @Override
    public String toString() {
        return "Append "  + " (" + super.getId() + ")";
    }
}
