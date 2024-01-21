package com.galsie.lib.utils;

import java.util.Optional;

public class NumericUtils {

    public static Optional<Double> parseDouble(String s){
        try{
            return Optional.of(Double.parseDouble(s));
        }catch(Exception ex){}
        return Optional.empty();
    }

    public static Optional<Integer> parseInt(String s){
        try{
            return Optional.of(Integer.parseInt(s));
        }catch(Exception ex){}
        return Optional.empty();
    }

    public static Optional<Long> parseLong(String s){

        try{
            return Optional.of(Long.parseLong(s));
        }catch(Exception ex){}
        return Optional.empty();
    }
}
