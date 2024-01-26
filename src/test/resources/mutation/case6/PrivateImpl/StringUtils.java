import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.MutationMessage;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class StringUtils {
    // Declare MutationMessage instances as static fields

    private static final MutationMessage firstWordMutation = new MutationMessage("Bitte füge Tests hinzu die das erste Wort testen. Schau dir hierzu nochmal die Vorlesung über inkrementelle Tests an.");
    private static final MutationMessage notFoundMutation = new MutationMessage("Bitte füge Tests hinzu, wo etwas gesucht wird, was nicht im String enthalten ist. Schau dir hierzu nochmal die Vorlesung über das Testen von Strings an.");
    String ret;
    Integer pos;

    public boolean isReverse(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(b.length() - i - 1)) {
                return false;
            }
        }
        return true;
    }

    public String replace(String source, String search, String replace) {
         ret = source;

        for(pos = ret.indexOf(search);(MutationInfrastructure.compute(new Pair<>(
            () -> ((
                pos >= 0
            )),
            new Variant<>(() -> ((
                pos > 0
            )), firstWordMutation)
        )));pos = ret.indexOf(search)) {
            ret = ret.substring(0, pos) + replace + ret.substring(pos + search.length());
        }
    
        final String result = ret;
        return MutationInfrastructure.compute(new Pair<>(
            () -> (result),
            new Variant<>(() -> (!result.equals(source) ? result : "-"), notFoundMutation)
        ));
    }

  
}