package eu.qped.racket.functions.lists;

import eu.qped.racket.test.Expression;

import java.util.List;

public class First extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(List<Expression> list) {
        return list.get(0).evaluate(this).substring(5).split("\\s")[1];
    }

    @Override
    public String toString() {
        return "first "  + " (" + super.getId() + ")";
    }
}
