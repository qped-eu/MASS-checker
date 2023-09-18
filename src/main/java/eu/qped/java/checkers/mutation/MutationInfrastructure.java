package eu.qped.java.checkers.mutation;

import java.util.ArrayList;
import java.util.List;

public class MutationInfrastructure {
    private static List<List<?>> listOfLists = new ArrayList<>();

    @SafeVarargs
    public static <R> R compute(MutationInterface<R>... s) {
        System.out.println("first method");
        return s[0].doit();
    }

    @SafeVarargs
    public static <R> R compute(Variant<R>... s) {
        List<Variant<R>> newList = new ArrayList<>();
        for (Variant<R> variant : s) {
            newList.add(variant);
        }
        listOfLists.add(newList);
        return s[0].getMutation().doit();
    }

    public static List<List<?>> getListOfLists() {
        return listOfLists;
    }


    void m() {
        int i = compute(
                new Variant<>(() -> 2, null),
                new Variant<>(() -> 3, null));


        int i2 = compute(
                () -> 1,
                () -> 2,
                () -> 3);


        long l = compute(
                new Variant<>(() -> 2, "Wrong"),
                new Variant<>(() -> 3, "Also Wrong"));
    }
}