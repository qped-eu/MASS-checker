package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Sub1 extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        return (float) list.get(0).evaluate(this) - (float) 1;
    }

    @Override
    public String toString() {
        return "Sub1" + "(" + super.getId() + ")";
    }
}
