package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class GreaterOrEqualThan extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        Boolean first = true;
        float value = 0;
        for (Expression e : list) {
            try {
                float valueNow = (float) e.evaluate(this);
                if (first) {
                    value = valueNow;
                    first = false;
                    continue;
                }
                if (!(value >= valueNow)) {
                    return false;
                }
                value = valueNow;
            } catch (ClassCastException ee) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "GreaterThanOrEqualThan" + "(" + super.getId() + ")";
    }
}
