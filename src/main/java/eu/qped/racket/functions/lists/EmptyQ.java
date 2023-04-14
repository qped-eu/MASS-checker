package eu.qped.racket.functions.lists;

import eu.qped.racket.test.Expression;

import java.util.List;

public class EmptyQ extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(List<Expression> list) {
        return Boolean.toString(list.get(0).getClass().equals(new Empty().getClass()));
    }

    @Override
    public String toString() {
        return "Empty? "  + " (" + super.getId() + ")";
    }
}
