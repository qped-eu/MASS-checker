import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Variant;

class MathUtilsCustom {
    public int add(int a, int b) {
        return (MutationInfrastructure.compute(() -> a+b, new Variant<>(() -> a-b, "a-b is also true. ERROR"), new Variant<>(() -> a*b, "a*b is also true. ERROR")));
    }
}

