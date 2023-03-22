package eu.qped.racket.functions;

import eu.qped.racket.test.Expression;
import eu.qped.racket.test.Parameter;

import java.util.HashMap;
import java.util.List;

public class CustomFunction extends Expression {
    private String funName;
    private List<Parameter> parameters;
    private Expression body;
    private HashMap hMap = new HashMap();

    public CustomFunction() {

    }

    public CustomFunction(String funName,List<Parameter> parameters, Expression body) {
        this.funName = funName;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public String evaluate(Expression e) {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    public String evaluate(List<Expression> list) {
        if (list.size() != parameters.size())
            System.out.println("FEHLER DIE ANZAHL AN PARAMETER Stimmt nicht überein");

        for (int i = 0 ; i < list.size(); i++) {
            //System.out.println(list.get(i).evaluate(this) + " in " + parameters.get(i));
            hMap.put(list.get(i).evaluate(this), parameters.get(i));
            parameters.get(i).setValue(list.get(i).evaluate(this));
        }

        //System.out.println(hMap);

        for (int i = 0; i < body.getParts().size(); i++) {
            if (body.getParts().get(i).getClass().equals(new Parameter(null).getClass())) {
                for (Parameter p : parameters) {
                    if (((Parameter)body.getParts().get(i)).getParaName().equals(p.getParaName()))
                        body.getParts().set(i,p);
                }
            }
        }
        //System.out.println(body.getParts());
        //parameters.stream().map(x -> x.evaluate(this)).forEach(System.out::println);
        Expression first = body.getParts().get(0);
        return first.evaluate(body.getParts().subList(1,body.getParts().size()));
    }

    public String getFunName() {
        return funName;
    }

    @Override
    public String toString() {
        return funName + "( " + parameters + " ) Body:\n" + body + "\nEnd Body";
    }
}
