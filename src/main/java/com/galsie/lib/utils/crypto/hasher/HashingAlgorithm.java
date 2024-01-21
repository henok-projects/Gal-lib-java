package com.galsie.lib.utils.crypto.hasher;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HashingAlgorithm {
    SHA256("SHA-256", 256);

    String algorithmIdentifier;
    int bitCount;

    /**
     * Compute the length required to represent the hashed output in the given base.
     *
     * For certain bases like 64 and 32 which use padding, the method takes into account
     * the necessary padding to the nearest multiple of 4 (for Base64) or 8 (for Base32).
     *
     * @param base the base in which to represent the hashed output
     * @return the length of the hashed output in the given base
     */
    public int getEncodedLength(int base){
        // 2^bitcount is the number of unique things we represent in base 2
        // representing the same number of ting in another base -> base^something = number of unique things
        // base^countForBase = 2^bitcount
        // countForBase*log(base) = bitcount*log(2)
        // countForBase = bitcount*log(2)/log(base)
        int count = (int) Math.ceil(bitCount * Math.log(2) / Math.log(base));

        // Determine the padding size based on the base
        int multiple = getSizeMultipleFor(base);
        int newCount = (count/multiple)*multiple;
        if (newCount < count){
            newCount += multiple;
        }
        return newCount;
    }

    private int getSizeMultipleFor(int base){
        if (base == 64) {
            return 4; // Base64 encoding pads to a multiple of 4
        } else if (base == 32) {
            return 8; // Base32 encoding pads to a multiple of 8
        }
        return 1;
    }


    /*
    public static void main(String... args){
        System.out.println("BASE 2 " + HashingAlgorithm.SHA256.getEncodedLength(2));
        System.out.println("BASE 16 " + HashingAlgorithm.SHA256.getEncodedLength(16));
        System.out.println("BASE 64 " + HashingAlgorithm.SHA256.getEncodedLength(64));
        var hash = Hasher.hashValue("Liwaa", HashingAlgorithm.SHA256, CodingAlgorithm.BASE64);
        System.out.println(hash.length());
    }*/
}
