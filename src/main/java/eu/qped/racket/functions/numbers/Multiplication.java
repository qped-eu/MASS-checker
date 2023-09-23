package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Multiplication extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Returns the result of multiplying the specified Numbers. Only Numbers are accepted as input.
     *
     * @param list List of operands
     * @return Multiplication of the given list
     * @throws Exception If a list entry is found that is not a Number, such as a Boolean or String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() == 0) {
            String stException = "expects atleast 1 argument, but found 0";
            throw new Exception(stException);
        }

        float result = 1;
        for (Expression e : list) {
            try {
                result *= (float) e.evaluate(this);

            } catch (ClassCastException ee) {
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Multiplication" + "(" + super.getId() + ")";
    }
}
