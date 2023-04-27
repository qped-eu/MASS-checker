package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Zero extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        return Boolean.toString(Float.valueOf(list.get(0).evaluate(this)) == 0);
    }

    @Override
    public String toString() {
        return "Zero?" + "(" + super.getId() + ")";
    }
}
