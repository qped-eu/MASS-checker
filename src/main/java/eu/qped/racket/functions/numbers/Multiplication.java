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
     * Das Ergebnis einer Multiplikation der angebenen Numbers wird zurückgegeben. Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return Multiplikation der übergebenen Liste
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
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
