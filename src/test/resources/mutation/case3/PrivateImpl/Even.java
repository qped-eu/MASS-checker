import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.List;

class Even {
	int counter = 0;

	Boolean v1 = true;
	Boolean v2 = false;

Even() {
	MutationInfrastructure.mutationMessageList.add("Was ist mit Zahlen wie 2, 4, etc.?");
	MutationInfrastructure.mutationMessageList.add("Was ist mit Zahlen wie 1, 3, etc.?");
}

	public boolean isTrue(int num) {
		System.out.println("HEllo from EVEN" + counter++);
		if (num % 2 == 0) {
			System.out.println("IN IF FALL");
			return MutationInfrastructure.compute(new Pair<>(
				() -> (true),
				new Variant<>(() -> false, "Was ist mit Zahlen wie 2, 4, etc.?",0)
			));
		} else {
			System.out.println("IN ELSE FALL");
			return MutationInfrastructure.compute(new Pair<>(
				() -> (false),
				new Variant<>(() -> true, "Was ist mit Zahlen wie 1, 3, etc.?",1)
		));
		}
	}	
}
