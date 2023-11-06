package eu.qped.racket.functions.lists;

import eu.qped.racket.buildingBlocks.Cons;
import eu.qped.racket.buildingBlocks.Expression;

import java.util.List;
import java.util.Stack;

public class Reverse extends Expression {
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

        String output = "";
        //List<String> inputs = new LinkedList<>();
        Stack<String> inputs = new Stack<>();
        if(list.get(0) instanceof Empty || (list.get(0).getParts().get(0) instanceof eu.qped.racket.functions.lists.List || list.get(0).getParts().get(0) instanceof Cons)) {
            String s = (String) list.get(0).evaluate(this);
            boolean next;
            String temp = "";
            for (String string : s.split(" ")) {
                if (string.compareTo("(cons") == 0)
                    continue;
                System.out.println(string);
                next = true;
                temp = "";
                for (Character c : string.toCharArray()) {
                    if (c == '\'') {
                        next = false;
                    }
                    if (next)
                        temp += c;
                }
                if (temp != "")
                    inputs.push(temp);
            }
            int counter = 0;
            while (!inputs.empty()) {
                output += "(cons " + inputs.pop() + " ";
                counter++;
            }
            output += "'()";
            while (counter > 0) {
                output += ")";
                counter--;
            }
            return output;
        } else {
            throw new Exception("Expression isnt instance of List");
        }
    }

    @Override
    public String toString() {
        return "Reverse "  + " (" + super.getId() + ")";
    }
}
