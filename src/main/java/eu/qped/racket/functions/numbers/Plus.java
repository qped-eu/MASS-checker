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
     * Das Ergebnis einer Addition der übergebenen Numbers wird berechnet. Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return Ergebnis der Addition
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
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