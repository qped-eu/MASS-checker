package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Range extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String s = "";
        float startpoint = (float) list.get(0).evaluate(this);
        float end = (float) list.get(1).evaluate(this);
        float stepsize = (float) list.get(2).evaluate(this);

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
