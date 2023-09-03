package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Round extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Eine Rundung der übergebenen Number wird berechnet(zu einem Integer). Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return Ergebnis der Rundung
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        try {
            float value1 = (float) list.get(0).evaluate(this);
            int value2 = (int) value1;
            if (value1 - value2 == 0.5) {
                result = (value2 % 2 == 0 ? value2 : value2 + 1);
            } else {
                result = Math.round((float) list.get(0).evaluate(this));
            }
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Round" + "(" + super.getId() + ")";
    }
}
