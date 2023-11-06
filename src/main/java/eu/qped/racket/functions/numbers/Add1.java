package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Add1 extends Expression {

    /**
     * This method evaluates the expression by incrementing the value of the first
     * element in the list of expressions by 1.
     *
     * @param e the Parent Expression
     * @return the result of the evaluation
     * @throws Exception if there's an issue with evaluation
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * This method evaluates the list of expressions by incrementing the value of
     * the first element by 1.
     *
     * @param list the list of expressions
     * @return the result of the evaluation
     * @throws Exception if there's an issue with evaluation
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        float result;
        try {
            result = (float) list.get(0).evaluate(this) + (float) 1;
        } catch (ClassCastException e) {
            try {
                result = Float.parseFloat((String) list.get(0).evaluate(this)) + 1.0f;
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result;
    }

    /**
     * Generates a string representation of the Add1 expression.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Add1" + "(" + super.getId() + ")";
    }
}
