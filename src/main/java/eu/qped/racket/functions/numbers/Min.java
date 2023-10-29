package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Min extends Expression {

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
     * Returns the minimum of the specified Numbers. Only Numbers are accepted as input.
     *
     * @param list List of Numbers to check
     * @return Minimum from the given list
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() == 0) {
            String stException = "expects atleast 1 argument, but found 0";
            throw new Exception(stException);
        }

        Boolean first = true;
        float result = 0;
        for (Expression e : list) {
           try {
                    float currentValue = (float) e.evaluate(this);
                    if (first) {
                        result = currentValue;
                        first = false;
                    }
                    if (result > currentValue) {
                        result = currentValue;
                    }
           } catch (ClassCastException ee){
               try {
                   float currentValue = 0;
                   if (e.evaluate(this) instanceof String) {
                       currentValue = Float.parseFloat((String) e.evaluate(this));
                   } else {
                       currentValue = (float) e.evaluate(this);
                   }
                   if (first) {
                       result = currentValue;
                       first = false;
                   }
                   if (result > currentValue) {
                       result = currentValue;
                   }
               } catch (NumberFormatException ex) {
                   String stException = "Expression isnt instance of Number/expects a float";
                   throw new Exception(stException);               }
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
        return "Min" + "(" + super.getId() + ")";
    }
}
