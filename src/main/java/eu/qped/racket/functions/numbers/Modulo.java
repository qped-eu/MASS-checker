package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Modulo extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float value1 = (float) list.get(0).evaluate(this);
        float value2 = (float) list.get(1).evaluate(this);
        float result = value1 % value2;
        return result == -0f ? 0 : result;  //Because java has -0 and Racket does not
    }

    @Override
    public String toString() {
        return "Modulo" + "(" + super.getId() + ")";
    }
}
