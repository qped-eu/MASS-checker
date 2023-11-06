package eu.qped.racket.functions.strings;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Parameter;

import java.util.List;
import java.util.Objects;

public class StringEQ extends Expression {


    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    /**
     * Checks if all String expressions in the list are equal.
     *
     * @param list List of expressions containing multiple Strings
     * @return True if all the String expressions are equal, false otherwise
     * @throws Exception If any expression in the list is not a String.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String firstString = null;
        boolean first = true;
        for (Expression e : list) {
            if (e instanceof Parameter) {
                String stException = "Expression isnt instance of String/expects a String";
                throw new Exception(stException);
            }
            try {
                if (first) {
                    firstString = (String) e.evaluate(this);
                    first = false;
                    continue;
                }
                if (!firstString.equals((String) e.evaluate(this))) {
                    return false;
                }
            } catch (ClassCastException ee) {
                String stException = "Expression isnt instance of String/expects a String";
                throw new Exception(stException);
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "StringEQ" + "(" + super.getId() + ")";
    }
}
