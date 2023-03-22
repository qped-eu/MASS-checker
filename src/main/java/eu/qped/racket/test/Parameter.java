package eu.qped.racket.test;


import eu.qped.racket.functions.CustomFunction;

import java.util.List;

public class Parameter extends CustomFunction {
    String paraName;
    String value;

    public Parameter(String paraName) {
        this.paraName = paraName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParaName() {
        return this.paraName;
    }

    @Override
    public String evaluate(Expression e) {
        return value;
    }

    public String evaluate(List<Expression> list) {
        return null;
    }

    @Override
    public String toString() {
        return "Parameter "+ paraName + " (" + super.getId() + ")";
    }

}
