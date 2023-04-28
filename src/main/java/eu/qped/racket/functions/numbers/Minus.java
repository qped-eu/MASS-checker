package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Minus extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        boolean first = true;
        float result = 0;
        for (Expression e : list) {
            if (first) {
                result = Float.valueOf(e.evaluate(this));
                first = false;
            } else
                result -= Float.valueOf(e.evaluate(this));
        }
        return Float.toString(result);
    }

    @Override
    public String toString() {
        return "Minus" + "(" + super.getId() + ")";
    }

}
