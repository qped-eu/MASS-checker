package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Positive extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Überprüft, ob die Number positiv ist. Es werden ausschließlich Numbers angenommen.
     * @param list  Liste der Operanden
     * @return  Boolean, ob Number positiv ist
     * @throws Exception    Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();          //Liste mit Operatoren, die eine Number zurückgeben
        int count = 0;                                      //Zähler, der die Anzahl der überprüften Operatoren mitzählt
        int value = 0;
        for (Class<?> clazz : opNum.arrayList) {
            count++;
            if (list.get(0) instanceof Number || clazz.isInstance(list.get(0).getParts().get(0))) {             //Liste mit "NumberOperatoren" wird durchlaufen und überprüft, ob der vorliegende Operator eine Number zurückgeben würde
                value = (int)(float) list.get(0).evaluate(this);       //Because Racket only accepts Integers in even?
                break;
            } else {                                        //Falls ein Operator innerhalb der list keine Number zurückgibt, also ein falscher Parameter übergeben wurde(!Number)
                if (opNum.arrayList.size() == count) {
                    throw new Exception("Expression isnt instance of Number");
                }
            }
        }
        return value > 0;
    }

    @Override
    public String toString() {
        return "Positive" + "(" + super.getId() + ")";
    }
}
