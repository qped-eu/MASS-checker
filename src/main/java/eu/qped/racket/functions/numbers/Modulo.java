package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Modulo extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Returns the remainder of an integer division between the two specified Numbers (Modulo operation). Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Remainder of the integer division
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        Float value1 = null;
        Float value2 = null;
        try {
            value1 = (float) list.get(0).evaluate(this);
            value2 = (float) list.get(1).evaluate(this);
            result = value1 % value2;
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return result == -0f ? 0 : result;      //Because java has -0 and Racket does not
    }


    @Override
    public String toString() {
        return "Modulo" + "(" + super.getId() + ")";
    }
}
