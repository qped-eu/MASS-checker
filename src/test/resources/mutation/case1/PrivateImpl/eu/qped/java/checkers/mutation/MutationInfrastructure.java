package eu.qped.java.checkers.mutation;

import java.util.ArrayList;
import java.util.List;

public class MutationInfrastructure {
    private static List<Pair<?>> listOfPairs = new ArrayList<>();
    private static boolean correctVariant=true;


    public static <R> R compute(Pair<R> variants) {
        listOfPairs.add(variants);
        if (correctVariant) {
            return variants.getCorrectVariant().doit();
        } else {
            return variants.getMutationVariant().getMutation().doit();
        }
    }

    public static List<Pair<?>> getListOfPairs() {
        return listOfPairs;
    }


    void m() {
        int i = compute(new Pair<>(() -> 1, new Variant<>(() -> 2, null)));


        long l = compute(new Pair<>(() -> 1, new Variant<>(() -> 2, "Wrong")));
    }
}
