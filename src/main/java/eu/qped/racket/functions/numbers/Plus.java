package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Plus extends Expression {


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
                String stException = "Expression isnt instance of Number/expects a float";
                throw new Exception(stException);
            }
        }
        return result;
    }

//    public GenericType evaluate(List<Expression> list) {
//        float result = 0;
//        for (Expression e : list) {
//            result += e.number
//        }
//        return Float.toString(result);
//        //return result;
//    }

    @Override
    public String toString() {
        return "Plus" + "(" + super.getId() + ")";
    }
}