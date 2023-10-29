package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Modulo extends Expression {

    /**
     * Evaluate the expression using the parent expression as input.
     *
     * @param e the Parent Expression
     * @return the maximum value obtained from evaluating the expression
     * @throws Exception if an error occurs during evaluation
     */
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
        if (list.size() != 2) {
            String stException = "expects only 2 arguments, but found " + list.size();
            throw new Exception(stException);
        }

        float result;
        Float value1;
        Float value2;
        try {
            value1 = (float) list.get(0).evaluate(this);
            value2 = (float) list.get(1).evaluate(this);
            result = value1 % value2;
        } catch (ClassCastException e) {
            try {
                if (list.get(0).evaluate(this) instanceof String) {
                    value1 = (float) Float.parseFloat((String) list.get(0).evaluate(this));
                } else {
                    value1 = (float) list.get(0).evaluate(this);
                }
                if (list.get(1).evaluate(this) instanceof String) {
                    value2 = (float) Float.parseFloat((String) list.get(1).evaluate(this));
                } else {
                    value2 = (float) list.get(1).evaluate(this);
                }
                result = value1 % value2;
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result == -0f ? 0 : result;      //Because java has -0 and Racket does not
    }

    /**
     * Generate a string representation of the Max expression.
     *
     * @return a string representation of the Max expression
     */
    @Override
    public String toString() {
        return "Modulo" + "(" + super.getId() + ")";
    }
}
