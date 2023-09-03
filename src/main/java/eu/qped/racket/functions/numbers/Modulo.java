package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Modulo extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Der Rest einer ganzzahligen Division von den beiden angebenen Numbers wird zurückgegeben(Modulo-Rechnung). Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return Rest einer ganzzahligen Division
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        Float value1 = null;
        Float value2 = null;
        try {
            value1 = (float) list.get(0).evaluate(this);
            value2 = (float) list.get(1).evaluate(this);
            result = value1 % value2;
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return result == -0f ? 0 : result;      //Because java has -0 and Racket does not
    }


    @Override
    public String toString() {
        return "Modulo" + "(" + super.getId() + ")";
    }
}
