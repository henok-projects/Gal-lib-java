package com.galsie.lib.utils;

import java.util.Optional;
import com.galsie.lib.utils.functional.ThrowableSupplier;
public class OptionalUtils {

    public static <R> Optional<R> ofThrowable(ThrowableSupplier<R> supplier){
        try{
           return  Optional.of(supplier.supply());
        }catch(Exception ex){

        }
        return Optional.empty();
    }
}
