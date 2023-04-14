package eu.qped.racket.functions.lists;

import eu.qped.racket.test.Expression;

import java.util.List;

public class Second extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(List<Expression> list) {
        return list.get(0).evaluate(this).split("cons ")[2].split("\\s")[0];
    }

    @Override
    public String toString() {
        return "second "  + " (" + super.getId() + ")";
    }
}
