package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class LessThan extends Expression {

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
            float valueNow = (float) e.evaluate(this);
            if (first) {
                value = valueNow;
                first = false;
                continue;
            }

            if (!(value < valueNow))
                return false;
            value = valueNow;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LessThan" + "(" + super.getId() + ")";
    }

}
