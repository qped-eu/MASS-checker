package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Plus extends Expression {


    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Das Ergebnis einer Addition der übergebenen Numbers wird berechnet. Es werden ausschließlich Numbers angenommen.
     * @param list  Liste der Operanden
     * @return  Ergebnis der Addition
     * @throws Exception    Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();              //Liste mit Operatoren, die eine Number zurückgeben
        float result = 0;
        int count = 0;                                          //Zähler, der die Anzahl der überprüften Operatoren mitzählt
        for (Expression e : list) {
            count = 0;
                for (Class<?> clazz : opNum.arrayList) {
                    count++;
                    if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {                 //Liste mit "NumberOperatoren" wird durchlaufen und überprüft, ob der vorliegende Operator eine Number zurückgeben würde
                        result += (float) e.evaluate(this);
                        break;
                    } else {                                                        //Falls ein Operator innerhalb der list keine Number zurückgibt, also ein falscher Parameter übergeben wurde(!Number)
                        if (opNum.arrayList.size() == count) {
                            throw new Exception("Expression isnt instance of Number");
                        }
                    }
                }
        }
        return result;
    }

//    public GenericType evaluate(List<Expression> list) {
//        float result = 0;
//        for (Expression e : list) {
//            result += e.number
//        }
//        return Float.toString(result);
//        //return result;
//    }

    @Override
    public String toString() {
        return "Plus" + "(" + super.getId() + ")";
    }
}