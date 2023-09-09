package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Sub1 extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Calculates the result of subtracting 1 from the given Number. Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Result of the subtraction by 1
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        try {
            result = (float) list.get(0).evaluate(this) - (float) 1;
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Sub1" + "(" + super.getId() + ")";
    }
}
