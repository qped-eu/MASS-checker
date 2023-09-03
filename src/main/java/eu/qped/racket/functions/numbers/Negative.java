package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Negative extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Überprüft, ob die Zahl negativ ist. Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return Boolean, ob Number negativ ist
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        int value = 0;
        try {
            value = (int) (float) list.get(0).evaluate(this);       //Because Racket only accepts Integers in even?
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return value < 0;
    }

    @Override
    public String toString() {
        return "Negative" + "(" + super.getId() + ")";
    }
}
