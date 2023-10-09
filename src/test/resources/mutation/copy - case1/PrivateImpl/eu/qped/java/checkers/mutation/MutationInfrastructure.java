package eu.qped.java.checkers.mutation;

import java.util.ArrayList;
import java.util.List;

public class MutationInfrastructure {
    private static List<Pair<?>> listOfPairs = new ArrayList<>();



    private static List<Boolean> correctVariants= new ArrayList<>();

    public static boolean isFirstTest() {
        return firstTest;
    }

    private static boolean firstTest = true;
    private static int currentIndex = 0;


    public static <R> R compute(Pair<R> variants) {
        listOfPairs.add(variants);
        if(firstTest) {
            return variants.getCorrectVariant().doit();
        } else {
            if (currentIndex < listOfPairs.size()) {
                boolean currentVariant = correctVariants.get(currentIndex);
                currentIndex++; // Increment the index for the next call
                if (currentVariant) {
                    return variants.getCorrectVariant().doit();
                } else {
                    System.out.println("INSIDE Mutation CLASS: " + correctVariants + " " + variants.getMutationVariant() + " Ergebnis: " + variants.getMutationVariant().getMutation().doit());
                    return variants.getMutationVariant().getMutation().doit();
                }
            } else {
                // Handle the case when there are no more elements in the list
                throw new IllegalStateException("No more elements in the list.");
            }
        }
    }

    public static List<Pair<?>> getListOfPairs() {
        return listOfPairs;
    }

    public static List<Boolean> getCorrectVariants() {
        return correctVariants;
    }

    public static void setCorrectVariants(List<Boolean> correctVariants) {
        MutationInfrastructure.correctVariants = correctVariants;
    }


    void m() {
        int i = compute(new Pair<>(() -> 1, new Variant<>(() -> 2, null)));


        long l = compute(new Pair<>(() -> 1, new Variant<>(() -> 2, "Wrong")));
    }
}
