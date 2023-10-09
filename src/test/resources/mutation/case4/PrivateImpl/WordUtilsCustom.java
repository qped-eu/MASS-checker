import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

public class WordUtilsCustom {

    WordUtilsCustom() {
        MutationInfrastructure.mutationMessageList.add("Du hast keine Großbuchstaben probiert");
    }
    public int countWordsBeginningWith(char c, String[] words) {
        int counter = 0;
        int i = 0;
        while (i < words.length) {
            String word = words[i];
            if(MutationInfrastructure.compute(new Pair<>(
				() -> (word.toUpperCase().charAt(0) == c || word.toLowerCase().charAt(0) == c),
				new Variant<>(() -> word.toUpperCase().charAt(0) == c, "Du hast keine Großbuchstaben probiert",0)
			)))
                counter++;
            }
            i++;
        return counter;
    }

}
