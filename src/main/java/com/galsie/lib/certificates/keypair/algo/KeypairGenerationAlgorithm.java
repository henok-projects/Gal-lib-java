package com.galsie.lib.certificates.keypair.algo;

import com.galsie.lib.certificates.SecurityProvider;
import lombok.AllArgsConstructor;

import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * A {@link KeypairGenerationAlgorithm} is an generation Algorithm with an Algorithm parameter spec
 * - An example algorithm is ECDSA
 * - An example Parameter spec is sec256rV1
 *
 * Each subclass MUST be an Algorithm
 * - Instances of the subclass are statically defined with the parameter spec
 * - Check {@link ECDSA} as an example
 */
@AllArgsConstructor
public abstract class KeypairGenerationAlgorithm {
    protected AlgorithmParameterSpec algorithmParameterSpec;

    /**
     * Gets the identifier of the algorithm, this is needed for getting a new kaypair genrator instace {@link java.security.KeyPairGenerator#getInstance(String, String)} finally building
     * @return A String identifying the algorithm
     */
    public abstract String getAlgorithmIdentifier();

    /**
     * Gets the Parameters used for generating the key with this algorithm
     * @return An AlgorithmParameterSpec which can be used with {@link java.security.KeyPairGenerator#initialize(AlgorithmParameterSpec, SecureRandom)} ()}
     */
    public AlgorithmParameterSpec getAlgorithmParameterSpec(){
        return this.algorithmParameterSpec;
    }


}