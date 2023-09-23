package eu.qped.racket.functions.strings;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.StringR;

import java.util.List;

public class StringQ extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    /**
     * Evaluates a list of expressions and checks if the first expression is an instance of 'StringR'.
     *
     * @param list List of expressions, expected to contain a single element
     * @return True if the first expression is an instance of 'StringR', false otherwise
     * @throws Exception If the list doesn't contain exactly one argument.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() > 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }
        if (list.get(0) instanceof StringR) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "StringQ" + "(" + super.getId() + ")";
    }
}
