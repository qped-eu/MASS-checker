package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Empty extends Expression {

    @Override
    public Object evaluate(Expression e) {
        return "'()";
    }

    @Override
    public Object evaluate(List<Expression> list) {

        return "";
    }

    @Override
    public String toString() {
        return "Empty "  + " (" + super.getId() + ")";
    }
}
