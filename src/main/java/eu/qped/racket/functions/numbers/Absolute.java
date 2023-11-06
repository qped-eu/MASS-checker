package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Absolute extends Expression {

    /**
     * This method evaluates the Absolute expression.
     *
     * @param e the Parent Expression
     * @return the absolute value of the expression
     * @throws Exception if evaluation encounters an issue
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    /**
     * This method evaluates the Absolute expression when given a list of expressions.
     *
     * @param list the list of expressions
     * @return the absolute value of the first expression
     * @throws Exception if evaluation encounters an issue
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        float result;
        try {
            result = (float) Math.abs((float) list.get(0).evaluate(this));
        } catch (ClassCastException e) {
            try {
                result = Math.abs(Float.parseFloat((String) list.get(0).evaluate(this)));
            } catch (NumberFormatException ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }

        }
        return result;
    }

    /**
     * This method returns the string representation of the Absolute expression.
     *
     * @return the string representation of the expression
     */
    @Override
    public String toString() {
        return "Absolute" + "(" + super.getId() + ")";
    }
}
