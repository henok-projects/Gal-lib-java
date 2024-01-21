package com.galsie.lib.certificates.keypair.algo;

import com.galsie.lib.certificates.SecurityProvider;
import org.bouncycastle.jce.ECNamedCurveTable;

import java.security.spec.AlgorithmParameterSpec;

public class ECDSA extends KeypairGenerationAlgorithm {

    /**
     * ECDSA Algorithms with Specs
     */
    public static ECDSA SECP_256_R1 = new ECDSA(ECNamedCurveTable.getParameterSpec("secp256r1"));

    /**
     * Algorithm Identifier is the same for all instances of the algorithm, what is changing is the parameter spec
     */
    private static String ALGORITHM_IDENTIFIER = "ECDSA";

    /**
     * Creates a new instance of the algorithm with the given parameter spec (such as what type of curve), and an algorithm provider (such as bouncy castle)
     * @param algorithmParameterSpec
     */
    private ECDSA(AlgorithmParameterSpec algorithmParameterSpec) {
        super(algorithmParameterSpec);
    }


    public String getAlgorithmIdentifier() {
        return ALGORITHM_IDENTIFIER;
    }
}