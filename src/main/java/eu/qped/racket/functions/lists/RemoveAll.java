package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.Arrays;

public class RemoveAll extends Expression {
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
        int count = (int) Arrays.stream(inputList.split(toRemove)).count();
        System.out.println(count);
        s = inputList.replace("(cons " + toRemove + " ", "");
        s = s.substring(0, s.length() - (count - 1));
        return s;
    }

    @Override
    public String toString() {
        return "Remove-All "  + " (" + super.getId() + ")";
    }
}
