package eu.qped.java.checkers.mutation;

import lombok.Getter;

@Getter
public class Pair<R> {
    private final MutationInterface<R> correctVariant;
    private final Variant<R> mutationVariant;

    public Pair(MutationInterface<R> correctVariant, Variant<R> mutationVariant) {
        this.correctVariant = correctVariant;
        this.mutationVariant = mutationVariant;
    }
}