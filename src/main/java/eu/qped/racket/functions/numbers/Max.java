package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Max extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() == 0) {
            String stException = "expects atleast 1 argument, but found 0";
            throw new Exception(stException);
        }

        Boolean first = true;
        float result = 0;
        for (Expression e : list) {
            try {
                    float currentValue = (float) e.evaluate(this);
                    if (first) {
                        result = currentValue;
                        first = false;
                    }
                    if (result < currentValue) {
                        result = currentValue;
                    }
                } catch (ClassCastException ee){
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Max" + "(" + super.getId() + ")";
    }
}
