package eu.qped.java.checkers.mutation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class MutationInfrastructure {

    @Getter
    @Setter
    private static List<Boolean> correctVariants = new ArrayList<>();

    @Getter
    @SuppressWarnings("FieldMayBeFinal")
    private static boolean firstTest = true;


    public static <R> R compute(Pair<R> variants) {
        if (firstTest) {
            return variants.getCorrectVariant().doit();
        } else {
            boolean currentVariant = correctVariants.get(variants.getMutationVariant().getMutationMessage().getMessageOrder());
            if (currentVariant) {
                return variants.getCorrectVariant().doit();
            } else {
                return variants.getMutationVariant().getMutation().doit();
            }
        }
    }
}
