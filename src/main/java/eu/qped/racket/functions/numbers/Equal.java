package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Equal extends Expression {

    /**
     * This method is used to evaluate the expression.
     *
     * @param e the Parent Expression
     * @return the result of the evaluation
     * @throws Exception if there's an evaluation error
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * This method is used to evaluate a list of expressions for equality.
     *
     * @param list the list of expressions to be evaluated
     * @return true if the expressions are equal, false otherwise
     * @throws Exception if there's an evaluation error
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() == 0) {
            String stException = "expects atleast 1 argument, but found 0";
            throw new Exception(stException);
        }

        boolean first = true;
        float value = 0;
        float valueNow;
        boolean resultBoolean = false;
        for (Expression e : list) {
            try {
                valueNow = (float) e.evaluate(this);
                System.out.println(valueNow);
                if (first) {
                    value = valueNow;
                    first = false;
                    resultBoolean = true;
                    continue;
                }
                float epsilon = 1 * (float) Math.pow(10, -10); //The accepted deviation for Float Values here 1*10^-10
                if (!(Math.abs(value - valueNow) < epsilon)) {
                    resultBoolean = false;
                }
                value = valueNow;
            } catch (ClassCastException ee) {
                try {
                    if (e.evaluate(this) instanceof String) {
                        valueNow = Float.parseFloat((String) e.evaluate(this));
                    } else {
                        valueNow = (float) e.evaluate(this);
                    }

                    if (first) {
                        value = valueNow;
                        first = false;
                        resultBoolean = true;
                        continue;
                    }
                    float epsilon = 1 * (float) Math.pow(10, -10); // The accepted deviation for Float Values, here set to 1*10^-10
                    // Check if the difference between the current value and the previous value is within epsilon
                    if (!(Math.abs(value - valueNow) < epsilon)) {
                        resultBoolean = false;
                    }
                    value = valueNow;
                } catch (Exception ex) {
                    String stException = "Expression isnt instance of Number/expects a float";
                    throw new Exception(stException);
                }
            }
        }
        return resultBoolean;
    }

    /**
     * This method provides a string representation of the Equal expression.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Equal" + "(" + super.getId() + ")";
    }

}
