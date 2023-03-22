package eu.qped.racket.functions.numbers;

import eu.qped.racket.test.Expression;

import java.util.List;

public class Round extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        float value1 = Float.valueOf(list.get(0).evaluate(this));
        int value2 = (int)value1;
        if (value1 - value2 == 0.5) {
            return Float.toString(value2 % 2 == 0 ? value2 : value2 + 1);
        }
        float value = (float)Math.round(Float.valueOf(list.get(0).evaluate(this)));
        return Float.toString(value);
    }

    @Override
    public String toString() {
        return "Round" + "(" + super.getId() + ")";
    }
}
