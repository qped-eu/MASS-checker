package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Even extends Expression {

    /**
     * This method evaluates the expression.
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
     * This method evaluates the expression given a list of expressions.
     *
     * @param list the list of expressions to evaluate
     * @return the result of the evaluation
     * @throws Exception if an evaluation error occurs
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        boolean resultBoolean = false;
        int value = 0;
        try {
            value = (int) (float) list.get(0).evaluate(this);       //Because Racket only accepts Integers in even?
            resultBoolean = value % 2 == 0;
        } catch (ClassCastException e) {
            try {
                // Evaluate the first expression and convert it to an integer
                value = (int)(float) Float.parseFloat((String)list.get(0).evaluate(this));       //Because Racket only accepts Integers in even?
                // Check if the value is even
                resultBoolean = value % 2 == 0;
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return resultBoolean;
    }

    /**
     * This method returns a string representation of the Even expression.
     *
     * @return a string representing the Even expression
     */
    @Override
    public String toString() {
        return "Even" + "(" + super.getId() + ")";
    }
}
