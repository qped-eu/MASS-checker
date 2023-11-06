package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Floor extends Expression {

    /**
     * This method evaluates the Floor expression.
     *
     * @param e the parent expression
     * @return the result of the Floor operation
     * @throws Exception if there's an evaluation error
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * This method evaluates the Floor expression for a list of expressions.
     *
     * @param list the list of expressions to evaluate
     * @return the result of the Floor operation
     * @throws Exception if there's an evaluation error
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        float result;
        try {
            result = (float) Math.floor((float) list.get(0).evaluate(this));
        } catch (ClassCastException e) {
            try {
                // Evaluate the first expression and convert it to an integer
                result = (float) Math.floor((float) Float.parseFloat((String)list.get(0).evaluate(this)));
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result == -0 ? 0 : result;
    }

    /**
     * This method returns a string representation of the Floor expression.
     *
     * @return the string representation of the expression
     */
    @Override
    public String toString() {
        return "Floor" + "(" + super.getId() + ")";
    }
}
