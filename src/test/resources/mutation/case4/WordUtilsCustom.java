import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

public class WordUtilsCustom {

    WordUtilsCustom() {
       // MutationInfrastructure.mutationMessageList.add("Also add some words which dont contain the letter");
        MutationInfrastructure.mutationMessageList.add("Also try words which start with an uppercase");
    }


    public int countWordsBeginningWith(char c, String[] words) {
        int counter = 0;
        for(int i = 0;i<words.length;i++) {
            String word = words[i];
            if(MutationInfrastructure.compute(new Pair<>(
				() -> (word.toUpperCase().charAt(0) == c || word.toLowerCase().charAt(0) == c),
				new Variant<>(() -> word.toLowerCase().charAt(0) == c, "Also try words which start with an uppercase",0)
			))) {
                counter++;
            }
            
        }
            
        return counter;
    }

}
