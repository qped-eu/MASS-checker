package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class GreaterOrEqualThan extends Expression {

    /**
     * This method evaluates the expression by calling the overloaded evaluate method.
     * It returns the result of evaluating the expression using the rest of the parent's parts.
     *
     * @param e the Parent Expression
     * @return the result of the evaluation
     * @throws Exception if an evaluation error occurs
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * This method evaluates a list of expressions to check if they satisfy the "greater than or equal" condition.
     * It iterates through the list of expressions, comparing their values to the previous value encountered.
     * If any value is not greater than or equal to the previous one, it returns false. Otherwise, it returns true.
     *
     * @param list the list of expressions to evaluate
     * @return true if all expressions satisfy the condition, false otherwise
     * @throws Exception if an evaluation error occurs
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() == 0) {
            String stException = "expects atleast 1 argument, but found 0";
            throw new Exception(stException);
        }

        Boolean first = true;
        float value = 0;
        for (Expression e : list) {
            try {
                float valueNow = (float) e.evaluate(this);
                if (first) {
                    value = valueNow;
                    first = false;
                    continue;
                }
                if (!(value >= valueNow)) {
                    return false;
                }
                value = valueNow;
            } catch (ClassCastException ee) {
                try {
                    float valueNow = 0;
                    if (e.evaluate(this) instanceof String) {
                        valueNow = Float.parseFloat((String) e.evaluate(this));
                    } else {
                        valueNow = (float) e.evaluate(this);
                    }
                    // If it's the first value encountered, set it as the reference value
                    if (first) {
                        value = valueNow;
                        first = false;
                        continue;
                    }

                    // Compare the current value with the previous one
                    if (!(value >= valueNow)) {
                        return false; // Return false if the condition is not satisfied
                    }
                    value = valueNow;
                } catch (Exception ex) {
                    String stException = "Expression isnt instance of Number/expects a float";
                    throw new Exception(stException);                }
            }
        }
        return true;
    }

    /**
     * This method returns a string representation of the GreaterOrEqualThan expression.
     *
     * @return the string representation of the expression
     */
    @Override
    public String toString() {
        return "GreaterThanOrEqualThan" + "(" + super.getId() + ")";
    }
}
