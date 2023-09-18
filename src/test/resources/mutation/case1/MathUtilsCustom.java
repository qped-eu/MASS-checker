import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

class MathUtilsCustom {
    public int add(int a, int b) {
        Variant v = new Variant<>(() -> a - b);
        if (MutationInfrastructure.compute(
                new Pair<>(() -> a > b, new Variant<>(() -> a > b, "a>=b is also true. ERROR")))) {
            return (MutationInfrastructure.compute(
                    new Pair<>(() -> a + b, new Variant<>(() -> a - b, "a-b is also true. ERROR"))));
        } else {
            return -1;
        }
    }
}