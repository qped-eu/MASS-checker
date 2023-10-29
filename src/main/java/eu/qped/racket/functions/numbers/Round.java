package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Round extends Expression {

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
     * Calculates the rounding of the given Number (to an Integer). Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Result of the rounding
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        float result;
        try {
            float value1 = (float) list.get(0).evaluate(this);
            int value2 = (int) value1;
            if (value1 - value2 == 0.5) {
                result = (value2 % 2 == 0 ? value2 : value2 + 1);
            } else {
                result = Math.round((float) list.get(0).evaluate(this));
            }
        } catch (ClassCastException e) {
            try {
                float value1 = (float) Float.parseFloat((String)list.get(0).evaluate(this));
                int value2 = (int)value1;
                if (value1 - value2 == 0.5) {
                    result = (value2 % 2 == 0 ? value2 : value2 + 1);
                } else {
                    result = Math.round((float) Float.parseFloat((String)list.get(0).evaluate(this)));
                }
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result;
    }

    /**
     * Generate a string representation of the Max expression.
     *
     * @return a string representation of the Max expression
     */
    @Override
    public String toString() {
        return "Round" + "(" + super.getId() + ")";
    }
}
