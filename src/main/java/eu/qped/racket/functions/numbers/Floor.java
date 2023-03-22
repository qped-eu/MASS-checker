package eu.qped.racket.functions.numbers;

import eu.qped.racket.test.Expression;

import java.util.List;

public class Floor extends Expression {

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        float value = (float)Math.floor(Float.valueOf(list.get(0).evaluate(this)));
        return Float.toString(value == -0 ? 0 : value);
    }

    @Override
    public String toString() {
        return "Floor" + "(" + super.getId() + ")";
    }
}
