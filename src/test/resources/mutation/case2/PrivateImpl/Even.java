import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.MutationMessage;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.List;

class Even {

	private static final MutationMessage mutationMessage0 = new MutationMessage("Was ist mit Zahlen wie 2, 4, etc.?");
	private static final MutationMessage mutationMessage1 = new MutationMessage("Was ist mit Zahlen wie 1, 3, etc.?");



	public boolean isTrue(int num) {
		if (num % 2 == 0) {
			return MutationInfrastructure.compute(new Pair<>(
				() -> (true),
				new Variant<>(() -> false, mutationMessage0)
			));
		} else {
			return MutationInfrastructure.compute(new Pair<>(
				() -> (false),
				new Variant<>(() -> true, mutationMessage1)
		));
		}
	}	
}
