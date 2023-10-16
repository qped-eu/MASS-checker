import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class StringUtils {
    static {
        MutationInfrastructure.mutationMessageList.add("use different elements in the list");
    }

    public static boolean isReverse(String a, String b) {
        if (a.length() != b.length()) {
            return false; // Wenn die Längen unterschiedlich sind, können sie nicht umgekehrt sein.
        }

        int length = a.length();
        for (int i = 0; i < length; i++) {
            if (a.charAt(i) != b.charAt(length - 1 - i)) {
                return false; // Vergleiche die Zeichen von vorne und hinten, um festzustellen, ob sie gleich sind.
            }
        }

        return true; // Wenn alle Zeichen übereinstimmen, sind die Strings umgekehrt.
    }

    public static String replace(String source, String search, String replace) {
        if (source == null || search == null || replace == null) {
            return null; // Rückgabe null, wenn eine der Eingaben null ist.
        }

        int searchLength = search.length();
        int sourceLength = source.length();
        StringBuilder result = new StringBuilder();
        int currentIndex = 0;

        while (currentIndex < sourceLength) {
            int nextIndex = source.indexOf(search, currentIndex);

            if (nextIndex == -1) {
                // Kein weiteres Vorkommen von search im Rest des source-Strings gefunden.
                result.append(source.substring(currentIndex));
                break;
            }

            // Alles zwischen currentIndex und nextIndex (exklusive) wird kopiert.
            result.append(source.substring(currentIndex, nextIndex));
            // Dann fügen wir den Ersatz (replace) hinzu.
            result.append(replace);
            currentIndex = nextIndex + searchLength;
        }

        return result.toString();
    }
}