package eu.qped.java.checkers.mutation;

public class MutationInfrastructure {

    @SafeVarargs
    static <R> R compute(MutationInterface<R>... s) {
        return s[0].doit();
    }
}
