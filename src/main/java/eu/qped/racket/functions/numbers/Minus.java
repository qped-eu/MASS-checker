package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Minus extends Expression {

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
    }

    /**
     * Calculates the result of subtracting the given Numbers. Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Result of the subtraction
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() == 0) {
            String stException = "expects atleast 1 argument, but found 0";
            throw new Exception(stException);
        }

        boolean first = true;
        float result = 0;
        for (Expression e : list) {
            try {
                    if (first) {
                        result = (float) e.evaluate(this);
                        first = false;
                    } else {
                        result -= (float) e.evaluate(this);
                    }
            } catch (ClassCastException ee){
                try {
                    if (first) {
                        // If it's the first expression, set the result to its evaluation
                        result = (float) Float.parseFloat((String) e.evaluate(this));
                        first = false;
                    } else {
                        result -= (float) Float.parseFloat((String) e.evaluate(this));
                    }
                } catch (Exception ex) {
                    String stException = "Expression isnt instance of Number/expects a float";
                    throw new Exception(stException);
                }
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
        return "Minus" + "(" + super.getId() + ")";
    }

}
