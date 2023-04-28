package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Empty extends Expression {

    @Override
    public String evaluate(Expression e) {
        return "'()";
    }

    @Override
    public String evaluate(List<Expression> list) {

        return "";
    }

    @Override
    public String toString() {
        return "Empty "  + " (" + super.getId() + ")";
    }
}
