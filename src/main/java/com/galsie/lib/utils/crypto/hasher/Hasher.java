package com.galsie.lib.utils.crypto.hasher;

import com.galsie.lib.utils.crypto.coder.Coder;
import com.galsie.lib.utils.crypto.coder.CodingAlgorithm;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hasher {

    @SneakyThrows
    public static String hashValue(String value, HashingAlgorithm hashingAlgorithm, CodingAlgorithm encodingAlgorithm){
        MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm.getAlgorithmIdentifier());
        byte[] hashedValue = digest.digest(
                value.getBytes(StandardCharsets.UTF_8));
        return Coder.encode(encodingAlgorithm, hashedValue);
    }
}
