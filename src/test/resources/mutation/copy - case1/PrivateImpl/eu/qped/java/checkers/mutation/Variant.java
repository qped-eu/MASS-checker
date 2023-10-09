package eu.qped.java.checkers.mutation;

public class Variant<R> {
    private final MutationInterface<R> mutation;
    private final String msg;

    public Variant(MutationInterface<R> mutation, String msg) {
        this.mutation = mutation;
        this.msg = msg;
    }

    public Variant(MutationInterface<R> mutation) {
        this.mutation = mutation;
        this.msg = null;
    }

    public MutationInterface<R> getMutation() {
        return mutation;
    }

    public String getMsg() {
        return msg;
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
