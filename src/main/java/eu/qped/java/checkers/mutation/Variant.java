package eu.qped.java.checkers.mutation;

public record Variant<R>(MutationInterface<R> variant, String msg) {
}
