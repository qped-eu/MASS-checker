package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Min extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Returns the minimum of the specified Numbers. Only Numbers are accepted as input.
     *
     * @param list List of Numbers to check
     * @return Minimum from the given list
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        Boolean first = true;
        float result = 0;
        for (Expression e : list) {
           try {
                    float currentValue = (float) e.evaluate(this);
                    if (first) {
                        result = currentValue;
                        first = false;
                    }
                    if (result > currentValue) {
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
        return "Min" + "(" + super.getId() + ")";
    }
}
