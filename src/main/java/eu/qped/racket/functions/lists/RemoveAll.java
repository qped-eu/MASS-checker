package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.Arrays;
import java.util.List;

public class RemoveAll extends Expression {
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
