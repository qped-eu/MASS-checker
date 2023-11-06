package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Ceiling extends Expression {

    /**
     * Evaluate the expression using the parent expression's data
     *
     * @param e the Parent Expression
     * @return the evaluation result
     * @throws Exception if an evaluation error occurs
     */
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Evaluate the expression using a list of expressions
     *
     * @param list the list of expressions to evaluate
     * @return the evaluation result
     * @throws Exception if an evaluation error occurs
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        float result = 0;
        try {
            result = (float) Math.ceil((float) list.get(0).evaluate(this));
        } catch (ClassCastException e) {
            try {
                result = (float) Math.ceil(Float.parseFloat((String) list.get(0).evaluate(this)));
            } catch (Exception ex) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result == -0 ? 0 : result;
    }

    @Override
    public String toString() {
        return "Ceiling" + "(" + super.getId() + ")";
    }
}
