package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Min extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Das Minimum von den angebenen Numbers wird zurückgegeben. Es werden ausschließlich Numbers angenommen.
     * @param list  Liste der zu überprüfenden Numbers
     * @return  Minimum aus der übergebenen Liste
     * @throws Exception    Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        OperatorNumbers opNum = new OperatorNumbers();      //Liste mit Operatoren, die eine Number zurückgeben
        Boolean first = true;
        float result = 0;
        int count = 0;                                      //Zähler, der die Anzahl der überprüften Operatoren mitzählt
        for (Expression e : list) {
            count = 0;
            for (Class<?> clazz : opNum.arrayList) {
                count++;
                if (e instanceof Number || clazz.isInstance(e.getParts().get(0))) {         //Liste mit "NumberOperatoren" wird durchlaufen und überprüft, ob der vorliegende Operator eine Number zurückgeben würde
                    float currentValue = (float) e.evaluate(this);
                    if (first) {
                        result = currentValue;
                        first = false;
                    }
                    if (result > currentValue) {
                        result = currentValue;
                    }
                    break;
                } else {                                                //Falls ein Operator innerhalb der list keine Number zurückgibt, also ein falscher Parameter übergeben wurde(!Number)
                    if (opNum.arrayList.size() == count) {
                        throw new Exception("Expression isnt instance of Number");
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Min" + "(" + super.getId() + ")";
    }
}
