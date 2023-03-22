package eu.qped.racket.functions.numbers;

import eu.qped.racket.test.Expression;

import java.util.List;

public class Equal extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        Boolean first = true;
        float value = 0;
        for (Expression e : list) {
            float valueNow = Float.valueOf(e.evaluate(this));
            if (first) {
                value = valueNow;
                first = false;
                continue;
            }
            float epsilon = 1 * (float)Math.pow(10, -10); //The accepted deviation vor Float Values here 1*10^-10
            if (!(Math.abs(value - valueNow) < epsilon))
                return Boolean.toString(false);
            value = valueNow;
        }
        return Boolean.toString(true);
    }

    @Override
    public String toString() {
        return "Equal" + "(" + super.getId() + ")";
    }

}
