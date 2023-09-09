package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Sqr extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Calculates the square of the given Number. Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Square of the Number
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        float value = 0;
        try {
            value = (float) list.get(0).evaluate(this);
            result = value * value;

        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Sqr" + "(" + super.getId() + ")";
    }
}
