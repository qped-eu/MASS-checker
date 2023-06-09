package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Sqrt extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        return (float) Math.sqrt((float) list.get(0).evaluate(this));
    }

    @Override
    public String toString() {
        return "Sqrt" + "(" + super.getId() + ")";
    }
}
