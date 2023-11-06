import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class StringUtils {
    String ret;
    Integer pos;
    static {
        MutationInfrastructure.mutationMessageList.add("also add tests for first word");
        MutationInfrastructure.mutationMessageList.add("also add tests for the case where search is not in source");
    }

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
         pos = ret.indexOf(search); // Capture 'pos' in 'finalPos'
        while (MutationInfrastructure.compute(new Pair<>(
            () -> ((
                pos = ret.indexOf(search)) >= 0
            ),
            new Variant<>(() -> ((
                pos = ret.indexOf(search)) > 0
            ), "also add tests for first word", 0)
        ))) {
            pos = ret.indexOf(search); // Update 'pos' with the captured value
            ret = ret.substring(0, pos) + replace + ret.substring(pos + search.length());
        }
    
        final String result = ret;
        return MutationInfrastructure.compute(new Pair<>(
            () -> (result),
            new Variant<>(() -> (!result.equals(source) ? result : "-"), "also add tests for the case where search is not in source", 1)
        ));
    }
    

  
}