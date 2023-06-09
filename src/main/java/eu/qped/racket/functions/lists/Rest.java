package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Rest extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String output = "";
        String s = (String) list.get(0).evaluate(this);
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
