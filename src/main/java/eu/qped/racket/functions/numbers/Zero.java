package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Zero extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Überprüft, ob die übergebene Number 0 ist. Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return Boolean, ob Number 0 ist
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        boolean returnBoolean = false;
        try {
            returnBoolean = ((float) (list.get(0).evaluate(this)) == 0);
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return returnBoolean;
    }

    @Override
    public String toString() {
        return "Zero?" + "(" + super.getId() + ")";
    }
}
