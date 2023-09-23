package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Odd extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Checks if the given Number is odd. Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Boolean indicating if the Number is odd
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        boolean resultBoolean;
        int value;
        try {
            value = (int) (float) list.get(0).evaluate(this);       //Because Racket only accepts Integers in even?
            resultBoolean = value % 2 != 0;
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return resultBoolean;
    }

    @Override
    public String toString() {
        return "Odd" + "(" + super.getId() + ")";
    }
}
