package eu.qped.racket.functions.lists;

import eu.qped.racket.test.Expression;

import java.util.List;

public class MakeList extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(List<Expression> list) {
        String s = "";
        float number = Float.valueOf(list.get(0).evaluate(this));
        String value = list.get(1).evaluate(this);
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
