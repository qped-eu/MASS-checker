package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Second extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        return list.get(0).evaluate(this).toString().split("cons ")[2].split("\\s")[0];
    }

    @Override
    public String toString() {
        return "second "  + " (" + super.getId() + ")";
    }
}
