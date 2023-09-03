package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Division extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        boolean first = true;
        float result = 0;
        for (Expression e : list) {
            try {
                if (first) {
                    result = (float) e.evaluate(this);
                    first = false;
                } else {
                    result = result / (float) e.evaluate(this);
                }
            } catch (ClassCastException ee) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Division" + "(" + super.getId() + ")";
    }
}
