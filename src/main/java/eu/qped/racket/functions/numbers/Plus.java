package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Plus extends Expression {


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
     * Calculates the result of adding the given Numbers. Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Result of the addition
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() == 0) {
            String stException = "expects atleast 1 argument, but found 0";
            throw new Exception(stException);
        }

        float result = 0;
        for (Expression e : list) {
            try {
                result += (float) e.evaluate(this);
            } catch (ClassCastException ee) {
                try {
                    result += (float) Float.parseFloat((String) e.evaluate(this));
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
        return "Plus" + "(" + super.getId() + ")";
    }
}