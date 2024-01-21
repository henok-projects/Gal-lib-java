package com.galsie.lib.utils.pair;

import com.galsie.lib.utils.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pair<T, V> {
    @Nullable
    private T first;

    @Nullable
    private V second;

    public boolean hasFirst(){
        return first != null;
    }

    public boolean hasSecond(){
        return second != null;
    }

    public static <T, V> Pair<T, V> of(T t, V v){
        return new Pair<>(t, v);
    }

    public static <T, V> Pair<T, V> ofSecond(V v){
        return Pair.of(null, v);
    }

    public static <T, V> Pair<T, V> ofFirst(T t){
        return Pair.of(t, null);
    }

}
