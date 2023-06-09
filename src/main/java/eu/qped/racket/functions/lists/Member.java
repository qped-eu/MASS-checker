package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Member extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        return Boolean.toString(list.get(1).evaluate(this).toString().contains((CharSequence) list.get(0).evaluate(this)));
    }

    @Override
    public String toString() {
        return "Member "  + " (" + super.getId() + ")";
    }
}
