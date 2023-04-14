package eu.qped.racket.functions.lists;

import eu.qped.racket.test.Expression;

public class Member extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(java.util.List<Expression> list) {
        return Boolean.toString(list.get(1).evaluate(this).contains(list.get(0).evaluate(this)));
    }

    @Override
    public String toString() {
        return "Member "  + " (" + super.getId() + ")";
    }
}
