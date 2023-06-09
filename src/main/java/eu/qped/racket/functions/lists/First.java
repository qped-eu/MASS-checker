package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class First extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        return list.get(0).evaluate(this).toString().substring(5).split("\\s")[1];
    }

    @Override
    public String toString() {
        return "first "  + " (" + super.getId() + ")";
    }
}
