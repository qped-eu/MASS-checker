package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Round extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float value1 = (float) list.get(0).evaluate(this);
        int value2 = (int)value1;
        if (value1 - value2 == 0.5) {
            return Float.toString(value2 % 2 == 0 ? value2 : value2 + 1);
        }
        float value = Math.round((float) list.get(0).evaluate(this));
        return value;
    }

    @Override
    public String toString() {
        return "Round" + "(" + super.getId() + ")";
    }
}
