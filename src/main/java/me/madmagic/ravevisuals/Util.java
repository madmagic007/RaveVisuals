package me.madmagic.ravevisuals;

import java.util.*;
import java.util.function.Consumer;

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
}
