import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class Lambda<T> {

    ArrayList<T> list = MutationInfrastructure.compute(new Pair<>(
		() -> (false),
		new Variant<>(() -> true, "Was ist mit Zahlen wie 1, 3, etc.?")
));

    // a) Methode zum Entfernen von Elementen aus der Liste basierend auf einem Filter
    public static <T> void removeIf(ArrayList<T> list, Predicate<T> filter) {
        list.removeIf(filter::test);
    }

    // b) Methode zum Sortieren der Liste basierend auf einem Comparator
    public static <T> void sortBy(ArrayList<T> list, Comparator<T> comparator) {
        MutationInfrastructure.compute(

        );
    }

    // c) Methode zum Konvertieren der Liste in einen String mit Zeilenumbrüchen
    public static <T> String listToString(ArrayList<T> list) {
        StringBuilder result = new StringBuilder();
        list.forEach(item -> result.append(item.toString()).append("\n"));
        return result.toString();
    }
}