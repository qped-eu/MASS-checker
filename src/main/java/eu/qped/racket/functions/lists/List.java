package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

public class List extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(java.util.List<Expression> list) {
        String s = "";
        int i = 0;
        for (Expression e: list) {
            s += "(cons " + e.evaluate(this) + " ";
            i++;
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
        return "List "  + " (" + super.getId() + ")";
    }
}
