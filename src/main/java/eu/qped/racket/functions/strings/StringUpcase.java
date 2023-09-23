package eu.qped.racket.functions.strings;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Parameter;

import java.util.List;

public class StringUpcase extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Converts a String expression to uppercase.
     *
     * @param list List of expressions containing a single String argument
     * @return Uppercased String
     * @throws Exception If the list doesn't contain exactly one argument or if the argument is not a String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }
        if (list.get(0) instanceof Parameter) {
            String stException = "Expression isnt instance of String/expects a String";
            throw new Exception(stException);
        }

        try {
            return ((String) list.get(0).evaluate(this)).toUpperCase();
        } catch (ClassCastException ee) {
            String stException = "Expression isnt instance of String/expects a String";
            throw new Exception(stException);
        }
    }

    @Override
    public String toString() {
        return "StringUpcase" + "(" + super.getId() + ")";
    }
}
