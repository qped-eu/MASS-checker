package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class LessThan extends Expression {

    /**
     * This method evaluates the LessThan expression.
     *
     * @param e the Parent Expression
     * @return the evaluation result (true if the condition is met, false otherwise)
     * @throws Exception if there's an issue during evaluation
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * This method evaluates the LessThan expression for a list of expressions.
     *
     * @param list the list of expressions to be evaluated
     * @return the evaluation result (true if all comparisons are true, false otherwise)
     * @throws Exception if there's an issue during evaluation
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
                    System.out.println("valueNow: " +valueNow);
                    System.out.println("value: " + value);
                    if (first) {
                        value = valueNow;
                        first = false;
                        continue;
                    }
                    if (!(value < valueNow)) {
                        return false;
                    }
                    value = valueNow;
            } catch (ClassCastException ee){
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
                    if (!(value < valueNow)) {
                        return false; // Return false if the condition is not satisfied
                    }
                    value = valueNow;
                } catch (Exception ex) {
                    String stException = "Expression isnt instance of Number/expects a float";
                    throw new Exception(stException);
                }
            }
        }
        return true;
    }

    /**
     * This method returns a string representation of the LessThan expression.
     *
     * @return the string representation of the expression
     */
    @Override
    public String toString() {
        return "LessThan" + "(" + super.getId() + ")";
    }

}
