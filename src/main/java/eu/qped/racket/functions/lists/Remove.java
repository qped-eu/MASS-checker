package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

public class Remove extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public String evaluate(java.util.List<Expression> list) {
        String s = "";
        String toRemove = list.get(0).evaluate(this);
        String inputList = list.get(1).evaluate(this);

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
