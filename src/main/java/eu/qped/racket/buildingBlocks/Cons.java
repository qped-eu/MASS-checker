package eu.qped.racket.buildingBlocks;

import java.util.List;

public class Cons extends Expression {
    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public String evaluate(List<Expression> list) {
        return "(cons " + list.get(0).evaluate(this) + " " + list.get(1).evaluate(this) + ")";
    }

    @Override
    public String toString() {
        return "Cons "  + " (" + super.getId() + ")";
    }
}
