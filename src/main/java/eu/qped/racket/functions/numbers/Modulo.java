package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Modulo extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        float value1 = Float.valueOf(list.get(0).evaluate(this));
        float value2 = Float.valueOf(list.get(1).evaluate(this));
        float result = value1 % value2;
        return Float.toString(result == -0f ? 0 : result);  //Because java has -0 and Racket does not
    }

    @Override
    public String toString() {
        return "Modulo" + "(" + super.getId() + ")";
    }
}
