package eu.qped.java.checkers.mutation;

import lombok.Getter;

@Getter
public class Variant<R> {
    private final MutationInterface<R> mutation;
    private final String msg;
    private final int order;

    public Variant(MutationInterface<R> mutation, String msg, int order) {
        this.mutation = mutation;
        this.msg = msg;
        this.order = order;
    }
    @Override
    public String toString() {
        return "Variant{" +
                "mutation=" + mutation +
                ", msg='" + msg + '\'' +
                '}';
    }
}
