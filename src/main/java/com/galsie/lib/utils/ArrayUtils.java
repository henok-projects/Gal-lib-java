package com.galsie.lib.utils;


import com.galsie.lib.utils.functional.ThrowableFunction;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayUtils {

    public static <T> T[] of(T... items){
        return items;
    }

    public static <T, R> List<R> mapCollectionThrows(Collection<T> from, ThrowableFunction<T, R> mapper) throws Exception {
        var res = new ArrayList<R>();
        for (var el: from){
            res.add(mapper.apply(el));
        }
        return res;
    }

    public static <T> boolean listsEqualIgnoreOrder(List<T> list1, List<T> list2){
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    public static <T> List<T> joinArrays(T[]... ts){
        return Arrays.stream(ts).flatMap(Arrays::stream).collect(Collectors.toList());
    }



}
