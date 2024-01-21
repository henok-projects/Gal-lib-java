package com.galsie.lib.utils.crypto.coder;

import java.util.Base64;
import java.util.HexFormat;

public class Coder {
    public static String encode(CodingAlgorithm algorithm, byte[] data){
        return switch (algorithm){
            case BASE16 -> HexFormat.of().formatHex(data);
            case BASE64 -> Base64.getEncoder().encodeToString(data);
        };
    }

    public static byte[] decode(CodingAlgorithm algorithm, String data){
        return switch (algorithm){
            case BASE16 -> HexFormat.of().parseHex(data);
            case BASE64 -> Base64.getDecoder().decode(data);
        };
    }
}
