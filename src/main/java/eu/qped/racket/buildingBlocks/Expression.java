package eu.qped.racket.buildingBlocks;

import java.util.LinkedList;
import java.util.List;

public class Expression {
    private List<Expression> parts = new LinkedList<>();
    private static int number = 0;
    private int id;

    public Expression() {
        id = number++;
    }

    /**
     * Evaluiert die Expression, wenn man diese Auffruft, soll man immer sich selber als verweis übergeben
     *
     * @param e the Parent Expression
     * @return Auswertung des ersten Wertes, eigentlich sollte der erste Wert ein Opperator sein,
     * der die anderen indierrekt abfragt deswegen sollte der erste reichen
     */
    public Object evaluate(Expression e) throws Exception {  //Expression
        if (parts.isEmpty()) {
            return "";
        }
        String s = "";
        System.out.println(parts);
        System.out.println("print parts : " + parts.get(0).getParts().toString());
        System.out.println("Evaluate: " + parts.get(0).evaluate(this));
        return parts.get(0).evaluate(this);
        //return parts.stream().map(x -> x.evaluate(this)).reduce((x,y) -> x + " , " + y).orElse(" FEHLER ");
    }

//    public Float evaluateFloat(Expression e) {
//        if (parts.isEmpty()) {
//            return "";
//        }
//        String s = "";
//        return parts.get(0).evaluate(this);
//        //return parts.stream().map(x -> x.evaluate(this)).reduce((x,y) -> x + " , " + y).orElse(" FEHLER ");
//    }

    public Object evaluate(List<Expression> list) throws Exception {
        return null;
    }

    /**
     * Ermöglicht es die nachfolgenen Expressions zu bekommen
     * @param id
     * @return
     */
    public List<Expression> getRest(int id) {
        List list = new LinkedList();
        for (Expression e : parts) {
            if (id < e.id) {
                list.add(e);
            }
        }

        return list;
    }

    public Expression addPart(Expression ex) {
        parts.add(ex);
        return ex;
    }

    public int getId() {
        return id;
    }

    public List<Expression> getParts() {
        return parts;
    }

    @Override
    public String toString() {
        return "\nExpression "+ id + "\n\t" + parts + "\n";
    }
}
