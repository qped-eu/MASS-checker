package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Positive extends Expression {

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
     * Checks if the Number is positive. Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Boolean indicating if the Number is positive
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        int value;
        try {
            value = (int) (float) list.get(0).evaluate(this);       //Because Racket only accepts Integers in even?
        } catch (ClassCastException e) {
            try {
                // Evaluate the first expression and convert it to an integer
                value = (int)(float) Float.parseFloat((String)list.get(0).evaluate(this));       //Because Racket only accepts Integers in even?
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return value > 0;
    }

    /**
     * Generate a string representation of the Max expression.
     *
     * @return a string representation of the Max expression
     */
    @Override
    public String toString() {
        return "Positive" + "(" + super.getId() + ")";
    }
}
