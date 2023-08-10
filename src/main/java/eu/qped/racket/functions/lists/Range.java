package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Range extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        String s = "";
        float startpoint;
        float end;
        try {                                                               //1 Statement vs 2 Statements handling
            startpoint = (float) list.get(0).evaluate(this);
            end = (float) list.get(1).evaluate(this);
        } catch (IndexOutOfBoundsException e){
            startpoint = 0;
            end = (float) list.get(0).evaluate(this);
        } catch (ClassCastException e){
            throw new Exception("Expression(Start/End) isnt Instance of Number");
        }

        float stepsize;
        try {                                                               //standard Stepsize versus 3rd Statement
            stepsize = (float) list.get(2).evaluate(this);
        } catch (IndexOutOfBoundsException e){
            stepsize = 1;
        } catch (ClassCastException e){
            throw new Exception("Expression(Stepsize) isnt Instance of Number");
        }

        int i = 0;
        while (startpoint < end) {
            s += "(cons " + startpoint + " ";
            i++;
            startpoint += stepsize;
        }
        s += "'()";
        while (i > 0) {
            s += ")";
            i--;
        }
        return s;
    }

    @Override
    public String toString() {
        return "Range "  + " (" + super.getId() + ")";
    }
}
