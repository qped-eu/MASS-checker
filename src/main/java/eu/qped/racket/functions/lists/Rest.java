package eu.qped.racket.functions.lists;

import eu.qped.racket.test.Expression;

import java.util.Arrays;

public class Rest extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(java.util.List<Expression> list) {
        String output = "";
        String s = list.get(0).evaluate(this);
        s = s.substring(1, s.length());
        Boolean rest = false;
        Boolean empty = false;
        for (Character c : s.toCharArray()) {
            if (rest) {
                output += c;
            } else {
                if (c == '(' || c == '\'') {
                    rest = true;
                    output += c;
                }
            }
        }
        return output.substring(0,output.length() - 1);
    }

    @Override
    public String toString() {
        return "Rest "  + " (" + super.getId() + ")";
    }
}
