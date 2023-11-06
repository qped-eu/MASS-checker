package eu.qped.racket.functions.strings;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Parameter;
import eu.qped.racket.buildingBlocks.StringR;

import java.util.List;

public class StringAppend extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }


    /**
     * Concatenates a list of String expressions and encloses the result in double quotes.
     *
     * @param list List of expressions, each expected to be a String
     * @return Concatenated String enclosed in double quotes
     * @throws Exception If any expression in the list is not a String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String resultString = "\"";
        for (Expression e : list) {
            if (e instanceof Parameter) {
                String stException = "Expression isnt instance of String/expects a String";
                throw new Exception(stException);
            }

            try {
                resultString = resultString.concat(((String) e.evaluate(this)).replace("\"", ""));
            } catch (ClassCastException ee) {
                String stException = "Expression isnt instance of String/expects a String";
                throw new Exception(stException);
            }
        }
        return resultString + "\"";
    }

    @Override
    public String toString() {
        return "StringAppend" + "(" + super.getId() + ")";
    }
}
