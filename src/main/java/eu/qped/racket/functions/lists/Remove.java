package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Remove extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String s = "";
        String toRemove = (String) list.get(0).evaluate(this);
        String inputList = (String) list.get(1).evaluate(this);

        if (!inputList.contains(toRemove)) {
            return inputList;
        }
        s = inputList.replaceFirst("cons " + toRemove + " ", "WÄASFASNJF123"); //Placeholder to remove (
        s = s.replace("(WÄASFASNJF123", "");
        s = s.substring(0, s.length() - 1);
        return s;
    }

    @Override
    public String toString() {
        return "Remove "  + " (" + super.getId() + ")";
    }
}
