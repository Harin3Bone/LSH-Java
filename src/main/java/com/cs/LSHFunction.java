package com.cs;

import java.util.Random;

public class LSHFunction {

    private final int[] randomIndexK;

    public LSHFunction(int dimensionD, int dimensionK, long randomSeed) {
        if (dimensionD <= 0) throw new IllegalArgumentException("dimensionD must be positive");
        if (dimensionK <= 0) throw new IllegalArgumentException("dimensionK must be positive");

        // 1. Generate vector K from K dimension
        this.randomIndexK = new int[dimensionK];
        for (var k = 0; k < dimensionK; k++) {
            randomIndexK[k] = new Random(randomSeed + k).nextInt(dimensionD);
        }
    }

    /**
     * Implement Hash Function
     *
     * @param vectorD the input vector in {0,1}^d
     * @return hash value in {0,1}^k
     */
    public String computeHash(int[] vectorD) {
        var sb = new StringBuilder();
        for (int kIndex : randomIndexK) {
            sb.append(vectorD[kIndex]);
        }

        return sb.toString();
    }

}
