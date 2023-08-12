package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Modulo extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Der Rest einer ganzzahligen Division von den beiden angebenen Numbers wird zurückgegeben(Modulo-Rechnung). Es werden ausschließlich Numbers angenommen.
     * @param list  Liste der Operanden
     * @return  Rest einer ganzzahligen Division
     * @throws Exception    Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();      //Liste mit Operatoren, die eine Number zurückgeben
        float result = 0;
        float value1 = 0;
        float value2 = 0;
        int count = 0;                  //Zähler, der die Anzahl der überprüften Operatoren mitzählt
        for (Class<?> clazz : opNum.arrayList) {
            count++;
            if ((list.get(0) instanceof Number || clazz.isInstance(list.get(0).getParts().get(0))) || (list.get(1) instanceof Number || clazz.isInstance(list.get(1).getParts().get(0)))) {     //Liste mit "NumberOperatoren" wird durchlaufen und überprüft, ob der vorliegende Operator eine Number zurückgeben würde
                value1 = (float) list.get(0).evaluate(this);
                value2 = (float) list.get(1).evaluate(this);
                result = value1 % value2;
                break;
            } else {                    //Falls ein Operator innerhalb der list keine Number zurückgibt, also ein falscher Parameter übergeben wurde(!Number)
                if (opNum.arrayList.size() == count) {
                    throw new Exception("Expression isnt instance of Number");
                }
            }
        }
        return result == -0f ? 0 : result;      //Because java has -0 and Racket does not
    }

    @Override
    public String toString() {
        return "Modulo" + "(" + super.getId() + ")";
    }
}
