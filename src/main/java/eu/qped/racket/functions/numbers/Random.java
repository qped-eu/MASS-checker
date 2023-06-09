package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Random extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        int max = (int) list.get(0).evaluate(this);
        return (new java.util.Random().nextInt(max));
    }

    @Override
    public String toString() {
        return "Random" + "(" + super.getId() + ")";
    }
}
