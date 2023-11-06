package eu.qped.racket.functions.strings;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Parameter;

import java.util.List;

public class StringContains extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }


    /**
     * Checks if one String expression contains another String expression.
     *
     * @param list List of expressions containing two Strings
     * @return Boolean indicating whether the second String contains the first String (as a substring)
     * @throws Exception If the list doesn't contain exactly two arguments or if any argument is not a String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {

        if (list.size() != 2) {
            String stException = "expects only 2 argument, but found " + list.size();
            throw new Exception(stException);
        }
        if (list.get(0) instanceof Parameter || list.get(1) instanceof Parameter) {
            String stException = "Expression isnt instance of String/expects a String";
            throw new Exception(stException);
        }

        try {
            return ((String) list.get(1).evaluate(this)).contains(((String) list.get(0).evaluate(this)).replace("\"", ""));
        } catch (ClassCastException ee) {
            String stException = "Expression isnt instance of String/expects a String";
            throw new Exception(stException);
        }
    }

    @Override
    public String toString() {
        return "StringContains" + "(" + super.getId() + ")";
    }
}
