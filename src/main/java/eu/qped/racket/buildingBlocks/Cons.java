package eu.qped.racket.buildingBlocks;

import java.util.List;

public class Cons extends Expression {
    @Override
    public Object evaluate(Expression e) {
        return evaluate((Expression) e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        return "(cons " +  list.get(0).evaluate(this) + " " + list.get(1).evaluate(this) + ")";             // (List<Expression>)
    }

    @Override
    public String toString() {
        return "Cons "  + " (" + super.getId() + ")";
    }
}
