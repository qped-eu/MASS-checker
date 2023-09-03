package eu.qped.racket.functions.numbers;

import eu.qped.racket.buildingBlocks.Expression;
import eu.qped.racket.buildingBlocks.Number;
import eu.qped.racket.buildingBlocks.OperatorNumbers;

import java.util.List;

public class Random extends Expression {

    @Override
    public Object evaluate(Expression e) throws Exception {
        return evaluate(e.getRest(super.getId()));
        //return evaluate(e.getNext(id), e.getNext(id+1));
    }

    /**
     * Es wird eine zufällige Number innerhalb der gegebenen Schranken berechnet. Es werden ausschließlich Numbers angenommen.
     *
     * @param list Liste der Operanden
     * @return zufällige Number
     * @throws Exception Es wird ein Listeneintrag, der keine Number, also zb ein Boolean oder String, ist, gefunden.
     */
    @Override
    public Object evaluate(List<Expression> list) throws Exception {
        float result = 0;
        float min = 0;
        float max = 0;
        try {                                                                                            //2 Statements
            min = (float) list.get(0).evaluate(this);
            max = (float) list.get(1).evaluate(this);
            if (min < max && (min % 1 == 0) && (max % 1 == 0)) {
//                        return min+(int)(Math.random()*((max-min)));
                return (int) (min + (new java.util.Random().nextInt((int) (max - min))));
            } else {
                throw new Exception("MaxValue/MinValue has decimals or !(min < max)");
            }
        } catch (IndexOutOfBoundsException e) {
            try {                                                                                        //1 Statement
                max = (float) list.get(0).evaluate(this);
                if (max > 0 && (max % 1 == 0)) {
//                            return (int) (Math.random()*(max));
                    return (int) (new java.util.Random().nextInt((int) (max)));
                } else {
                    throw new Exception("MaxValue has decimals or is <=0");
                }
            } catch (IndexOutOfBoundsException ee) {                                                     //0 Statements
                result = (float) Math.random();
            } catch (ClassCastException cc) {
                throw new Exception("Expression isnt instance of Number/expects a float");
            }
        } catch (ClassCastException cc) {
            throw new Exception("Expression isnt instance of Number/expects a float");
        }
        return result;
    }

    @Override
    public String toString() {
        return "Random" + "(" + super.getId() + ")";
    }
}
