package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Equal extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        Boolean first = true;
        float value = 0;
        for (Expression e : list) {
            float valueNow = (float) (e.evaluate(this));
            if (first) {
                value = valueNow;
                first = false;
                continue;
            }
            float epsilon = 1 * (float)Math.pow(10, -10); //The accepted deviation vor Float Values here 1*10^-10
            if (!(Math.abs(value - valueNow) < epsilon))
                return false;
            value = valueNow;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Equal" + "(" + super.getId() + ")";
    }

}
