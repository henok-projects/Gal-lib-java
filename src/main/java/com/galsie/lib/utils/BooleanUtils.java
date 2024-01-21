package com.galsie.lib.utils;

import java.util.Optional;

public class BooleanUtils {

    public static Optional<Boolean> parseBoolean(String s){
        try{
            return Optional.of(Boolean.parseBoolean(s));
        }catch(Exception ex){
        }
        return Optional.empty();
    }
}
