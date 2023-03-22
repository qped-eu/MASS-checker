package eu.qped.racket.functions.numbers;

import eu.qped.racket.test.Expression;

import java.util.List;

public class Random extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        int max = (int)(float)Float.valueOf(list.get(0).evaluate(this));
        return Integer.toString(new java.util.Random().nextInt(max));
    }

    @Override
    public String toString() {
        return "Random" + "(" + super.getId() + ")";
    }
}
