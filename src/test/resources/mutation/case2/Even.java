import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.List;

class Even {

static {
	MutationInfrastructure.mutationMessageList.add("Was ist mit Zahlen wie 2, 4, etc.?");
	MutationInfrastructure.mutationMessageList.add("Was ist mit Zahlen wie 1, 3, etc.?");
}

	public boolean isTrue(int num) {
		if (num % 2 == 0) {
			return MutationInfrastructure.compute(new Pair<>(
				() -> (true),
				new Variant<>(() -> false, "Was ist mit Zahlen wie 2, 4, etc.?",0)
			));
		} else {
			return MutationInfrastructure.compute(new Pair<>(
				() -> (false),
				new Variant<>(() -> true, "Was ist mit Zahlen wie 1, 3, etc.?",1)
		));
		}
	}	
}
