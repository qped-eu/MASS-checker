import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;

class Negative {
    public boolean isNegative(int num) {
        if (num >= 0) {
            return (MutationInfrastructure.compute(() -> false));
        } else {
            return true;
        }
    }
}
