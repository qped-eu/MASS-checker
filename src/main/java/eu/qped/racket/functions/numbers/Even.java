package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Even extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        int value = (int)(list.get(0).evaluate(this));    //Because Racket only accepts Integers in even?
        return value % 2 == 0;
    }

    @Override
    public String toString() {
        return "Even" + "(" + super.getId() + ")";
    }
}
