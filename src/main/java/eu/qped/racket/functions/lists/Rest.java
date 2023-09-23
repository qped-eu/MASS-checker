package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Cons;
import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;

public class Rest extends Expression {
    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
    }

    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        if (list.size() != 1) {
            String stException = "expects only 1 argument, but found " + list.size();
            throw new Exception(stException);
        }

        if((list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0).getParts().get(0) instanceof Cons)) {
            String output = "";
            String s = (String) list.get(0).evaluate(this);
            s = s.substring(1);
            boolean rest = false;
            for (Character c : s.toCharArray()) {
                if (rest) {
                    output += c;
                } else {
                    if (c == '(' || c == '\'') {
                        rest = true;
                        output += c;
                    }
                }
            }
            return output.substring(0, output.length() - 1);
        } else {
            throw new Exception("Expression isnt instance of List");
        }
    }

    @Override
    public String toString() {
        return "Rest "  + " (" + super.getId() + ")";
    }
}
