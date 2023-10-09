import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

class MathUtilsCustom {
      public int countWordsBeginningWith(char c, String[] words) {
        int counter = 0;
        int i = 0;
        while (i < words.length) {
            String word = words[i];
            if (word.toUpperCase().charAt(0) == c || word.toLowerCase().charAt(0) == c) {
                counter++;
            }
            i++;
        }
        return counter;
    }
}