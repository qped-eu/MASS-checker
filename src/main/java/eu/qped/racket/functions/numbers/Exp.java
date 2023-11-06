package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Exp extends Expression {

    /**
     * This method evaluates the expression by calling the overloaded evaluate method with a modified argument.
     * It is assumed that super.getId() returns the ID of the current instance of the Exp class.
     *
     * @param e the Parent Expression
     * @return the result of the evaluation
     * @throws Exception if an error occurs during evaluation
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * This method evaluates a list of expressions.
     *
     * @param list the list of expressions to evaluate
     * @return the result of the evaluation
     * @throws Exception if an error occurs during evaluation
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        float result;
        try {
            result = (float) Math.exp((float) list.get(0).evaluate(this));
        } catch (ClassCastException e) {
            try {
                // Evaluate the first expression and convert it to an integer
                result = (float) Math.exp((float) Float.parseFloat((String)list.get(0).evaluate(this)));
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result;
    }

    /**
     * This method returns a string representation of the Exp object.
     *
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "Exp" + "(" + super.getId() + ")";
    }
}
