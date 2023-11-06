package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Division extends Expression {

    /**
     * Evaluates the given Expression by delegating to another version of "evaluate".
     *
     * @param e the Parent Expression
     * @return the evaluation result
     * @throws Exception if an error occurs during evaluation
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Evaluates a list of Expressions by performing a division operation.
     *
     * @param list the list of Expressions to evaluate
     * @return the result of the division operation
     * @throws Exception if an error occurs during evaluation
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
                    result = result / (float) e.evaluate(this);
                }
            } catch (ClassCastException ee) {
                try {
                    if (first) {
                        // If it's the first expression, set the result to its evaluation
                        result = (float) Float.parseFloat((String) e.evaluate(this));
                        first = false;
                    } else {
                        // If it's not the first expression, perform division
                        result = result / Float.parseFloat((String) e.evaluate(this));
                    }
                } catch (NumberFormatException ex) {
                    String stException = "Expression isnt instance of Number/expects a float";
                    throw new Exception(stException);
                }
            }
        }
        return result;
    }

    /**
     * Generates a string representation of the Division object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Division" + "(" + super.getId() + ")";
    }
}
