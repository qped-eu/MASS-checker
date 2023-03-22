package eu.qped.racket.test;

import com.ibm.icu.impl.Pair;

public class Cons extends Expression {
    private final Pair<Expression, Expression> value;

    public Cons(Pair value) {
        this.value = value;
    }

    @Override
    public String evaluate(Expression e) {
        String s1 = value.first.evaluate(this);
        String s2 = value.second.evaluate(this);
        return s2 == "" ? "'(" + s1 + ")" : "'(" + s1 + " . " + s2 + ")";
    }

    @Override
    public String toString() {
        return "Cons " + value + " (" + super.getId() + ")";
    }
}
