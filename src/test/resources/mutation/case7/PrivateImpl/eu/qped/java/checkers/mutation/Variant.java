package eu.qped.java.checkers.mutation;

import lombok.Getter;

@Getter
public class Variant<R> {
    private final MutationInterface<R> mutation;
    private final MutationMessage mutationMessage;

    public Variant(MutationInterface<R> mutation, MutationMessage mutationMessage) {
        this.mutation = mutation;
        this.mutationMessage = mutationMessage;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "mutation=" + mutation +
                ", mutationMessage=" + mutationMessage.getMessage() +
                '}';
    }
}
