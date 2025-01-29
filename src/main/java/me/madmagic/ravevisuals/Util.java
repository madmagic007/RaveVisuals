package me.madmagic.ravevisuals;

import org.bukkit.Color;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Util {

    public static <T> void runIfNotNull(T obj, Consumer<T> notNull) {
        runIfNotNull(obj, notNull, () -> {});
    }

    public static <T> void runIfNotNull(T obj, Consumer<T> notNull, Runnable isNull) {
        if (obj == null) isNull.run();
        else notNull.accept(obj);
    }

    public static <T> List<T> listNotNull(List<T> list) {
        return listNotNull(list, () -> {});
    }

    public static <T> List<T> listOnlyNullCheck(List<T> list, Runnable isNull) {
        if (list == null) isNull.run();
        return list;
    }

    public static <T> List<T> listNotNull(List<T> list, Consumer<List<T>> action) {
        if (list == null) list = new ArrayList<>();
        action.accept(list);
        return list;
    }

    public static <T> Set<T> listNotNull(Set<T> list, Consumer<Set<T>> action) {
        if (list == null) list = new HashSet<>();
        action.accept(list);
        return list;
    }

    public static <T> List<T> listNotNull(List<T> list, Runnable isNull) {
        if (list == null) {
            isNull.run();
            return new ArrayList<>();
        }
        return list;
    }

    public static <T> void foreachPopulateIfEmpty(T[] arr, Supplier<? extends Collection<? extends T>> supplier, Consumer<T> consumer) {
        ArrayList<T> list = new ArrayList<>();
        if (arr.length == 0) list.addAll(supplier.get());
        else list.addAll(Arrays.asList(arr));

        list.forEach(consumer);
    }

    public static Color hexToColor(String hex) {
        hex = hex.replaceFirst("#", "");

        return Color.fromRGB(
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16)
        );
    }
}
