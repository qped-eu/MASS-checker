import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.MutationMessage;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.List;

class MathUtils {

	private static final MutationMessage mutationMessage0 = new MutationMessage("add more than a");

	public int multato5(int a) {
		int result = MutationInfrastructure.compute(new Pair<>(
            () -> (5*a),
            new Variant<>(() -> (5*0), mutationMessage0)
        ));
		return result;
	}	
}
