package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Length extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        return Float.toString(list.get(0).evaluate(this).toString().split("cons").length - 1);
    }

    @Override
    public String toString() {
        return "Length "  + " (" + super.getId() + ")";
    }
}
