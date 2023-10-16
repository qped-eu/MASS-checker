package eu.qped.java.checkers.mutation;

import java.util.ArrayList;
import java.util.List;

public class MutationInfrastructure {
    private static List<Pair<?>> listOfPairs = new ArrayList<>();
    public static List<String> mutationMessageList = new ArrayList<>();


    private static List<Boolean> correctVariants= new ArrayList<>();

    public static boolean isFirstTest() {
        return firstTest;
    }

    private static boolean firstTest = true;
    private static int currentIndex = 0;
    private static String currentMessage = "";


    public static <R> R compute(Pair<R> variants) {
        if(firstTest) {
            return variants.getCorrectVariant().doit();
        } else {
            if (currentIndex < 2) {
                boolean currentVariant = correctVariants.get(variants.getMutationVariant().getOrder());
                currentMessage = mutationMessageList.get(variants.getMutationVariant().getOrder());
                if (currentVariant) {
                    System.out.println("THIS IS A CURRENT VARIANT: ");
                    return variants.getCorrectVariant().doit();
                } else {
                    System.out.println("THIS IS A MESSAGE: " + variants.getMutationVariant().getMsg());
                    //currentMessage = variants.getMutationVariant().getMsg();
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
        int i = compute(new Pair<>(() -> 1, new Variant<>(() -> 2, null,5)));


        long l = compute(new Pair<>(() -> 1, new Variant<>(() -> 2, "Wrong",6)));
    }
}
