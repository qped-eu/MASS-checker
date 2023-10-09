package eu.qped.java.checkers.mutation;

public class Variant<R> {
    private final MutationInterface<R> mutation;
    private final String msg;
    private final int order;

    public Variant(MutationInterface<R> mutation, String msg, int order) {
        this.mutation = mutation;
        this.msg = msg;
        this.order = order;
    }

    public Variant(MutationInterface<R> mutation) {
        this.mutation = mutation;
        this.msg = null;
        this.order = -1;
    }

    public MutationInterface<R> getMutation() {
        return mutation;
    }

    public String getMsg() {
        return msg;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variant<?> variant = (Variant<?>) o;

        if (!mutation.equals(variant.mutation)) return false;
        return msg.equals(variant.msg);
    }

    @Override
    public int hashCode() {
        int result = mutation.hashCode();
        result = 31 * result + msg.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Variant{" +
                "mutation=" + mutation +
                ", msg='" + msg + '\'' +
                '}';
    }
}
