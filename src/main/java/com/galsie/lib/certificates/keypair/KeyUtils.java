package com.galsie.lib.certificates.keypair;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class KeyUtils {

    public static PrivateKey decodePrivateKey(byte[] keyData, String keyAlgo) throws Exception {
        var encodedKeySpec = new PKCS8EncodedKeySpec(keyData);
        var keyFactory = KeyFactory.getInstance(keyAlgo);
        return keyFactory.generatePrivate(encodedKeySpec);
    }
}
