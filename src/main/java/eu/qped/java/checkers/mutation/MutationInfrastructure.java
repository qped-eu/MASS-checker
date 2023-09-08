package eu.qped.java.checkers.mutation;

import eu.qped.racket.main;

public class MutationInfrastructure {

    @SafeVarargs
    public static <R> R compute(MutationInterface<R>... s) {

        return s[0].doit();
    }

    @SafeVarargs
    public static <R> R compute(MutationInterface<R> correctVariant, Variant<R>... s) {
        return s[0].variant().doit();
    }

    void m() {
        int i = compute(
                () -> 1,
                new Variant<>(() -> 2, null),
                new Variant<>(() -> 3, null));

        int i2 = compute(
                () -> 1,
                () -> 2,
                () -> 3);


        long l = compute(
                () -> 1,
                new Variant<>(() -> 2, "Wrong"),
                new Variant<>(() -> 3, "Also Wrong"));
    }
}
