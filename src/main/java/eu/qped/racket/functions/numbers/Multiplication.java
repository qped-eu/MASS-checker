package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Multiplication extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 1;
        for (Expression e : list) {
            result *= (float) e.evaluate(this);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Multiplication" + "(" + super.getId() + ")";
    }
}
