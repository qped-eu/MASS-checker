package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Sqrt extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Die Quadratwurzel der übergebenen Number wird berechnet. Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return Quadratwurzel der Number
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        try {
            if (((float) list.get(0).evaluate(this)) >= 0) {
                result = (float) Math.sqrt((float) list.get(0).evaluate(this));
            } else {
                throw new Exception("Number is negative");                          //Racket verwendet komplexe Zahlen?
            }
        } catch (ClassCastException e) {
            String stException = "Expression isnt instance of Number/expects a float";
            throw new Exception(stException);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Sqrt" + "(" + super.getId() + ")";
    }
}
