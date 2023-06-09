package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class MakeList extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String s = "";
        float number = (float) list.get(0).evaluate(this);
        String value = (String) list.get(1).evaluate(this);
        int counter = 0;
        while (number > 0) {
            s += "(cons " + value + " ";
            number--;
            counter++;
        }
        s += "'()";
        while (counter > 0) {
            s += ")";
            counter--;
        }
        return s;
    }

    @Override
    public String toString() {
        return "make-list "  + " (" + super.getId() + ")";
    }
}
