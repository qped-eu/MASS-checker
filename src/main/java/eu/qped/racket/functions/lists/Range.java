package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

public class Range extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(java.util.List<Expression> list) {
        String s = "";
        float startpoint = Float.valueOf(list.get(0).evaluate(this));
        float end = Float.valueOf(list.get(1).evaluate(this));
        float stepsize = Float.valueOf(list.get(2).evaluate(this));

        int i = 0;
        while (startpoint < end) {
            s += "(cons " + startpoint + " ";
            i++;
            startpoint += stepsize;
        }
        s += "'()";
        while (i > 0) {
            s += ")";
            i--;
        }
        return s;
    }

    @Override
    public String toString() {
        return "Range "  + " (" + super.getId() + ")";
    }
}
