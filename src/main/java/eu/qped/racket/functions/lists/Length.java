package eu.qped.racket.functions.lists;

import eu.qped.racket.test.Expression;

import java.util.List;

public class Length extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        return Float.toString(list.get(0).evaluate(this).split("cons").length - 1);
    }

    @Override
    public String toString() {
        return "Length "  + " (" + super.getId() + ")";
    }
}
