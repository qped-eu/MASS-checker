import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

class Even {
	Boolean v1 = MutationInfrastructure.compute(new Pair<>(
		() -> (true),
		new Variant<>(() -> false, "Was ist mit Zahlen wie 2, 4, etc.?")
	));
	Boolean v2 = MutationInfrastructure.compute(new Pair<>(
		() -> (false),
		new Variant<>(() -> true, "Was ist mit Zahlen wie 1, 3, etc.?")
));


	public boolean isTrue(int num) {
		if (num % 2 == 0) {
			return v1;
		} else {
			return v2;
		}
	}	
}
