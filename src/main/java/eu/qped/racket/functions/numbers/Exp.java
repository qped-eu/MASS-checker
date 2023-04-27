package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Exp extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        float value = Float.valueOf(list.get(0).evaluate(this));
        return Float.toString((float)Math.exp(value));
    }

    @Override
    public String toString() {
        return "Exp" + "(" + super.getId() + ")";
    }
}
