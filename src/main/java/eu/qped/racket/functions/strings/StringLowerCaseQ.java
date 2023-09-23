package eu.qped.racket.functions.strings;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Parameter;

import java.util.List;

public class StringLowerCaseQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }


    /**
     * Checks if a String expression is equal to its lowercase version.
     *
     * @param list List of expressions containing a single String argument
     * @return True if the String expression is equal to its lowercase version, false otherwise
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
            return ((String) list.get(0).evaluate(this)).equals(((String) list.get(0).evaluate(this)).toLowerCase());
        } catch (ClassCastException ee) {
            String stException = "Expression isnt instance of String/expects a String";
            throw new Exception(stException);
        }
    }

    @Override
    public String toString() {
        return "StringLowerCaseQ" + "(" + super.getId() + ")";
    }
}
