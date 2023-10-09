import eu.qped.java.checkers.mutation.MutationInfrastructure;
import eu.qped.java.checkers.mutation.MutationInterface;
import eu.qped.java.checkers.mutation.Pair;
import eu.qped.java.checkers.mutation.Variant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class Lambda<T> {
    Lambda() {
        MutationInfrastructure.mutationMessageList.add("use different elements in the list");
    }

    // a) Methode zum Entfernen von Elementen aus der Liste basierend auf einem Filter
    public static <T> void removeIf(ArrayList<T> list, Predicate<T> filter) {
        list.removeIf(filter::test);
    }

    // b) Methode zum Sortieren der Liste basierend auf einem Comparator
    public static <T> void sortBy(ArrayList<T> list, Comparator<T> comparator) {
        list.sort(comparator);
        MutationInfrastructure.compute(new Pair<>(
            () -> {
                list.sort(comparator);
                System.out.println("List sorted successfully");
            },
            new Variant<>() // Using the constructor for Void
            , "use different elements in the list", 0)
        );
        
    }

    // c) Methode zum Konvertieren der Liste in einen String mit Zeilenumbrüchen
    public static <T> String listToString(ArrayList<T> list) {
        StringBuilder result = new StringBuilder();
        list.forEach(item -> result.append(item.toString()).append("\n"));
        return result.toString();
    }
}