package com.cs;

import java.util.Random;

public class LSHFunction {

    private final int dimensionD;
    private final int dimensionK;
    private final long randomSeed;

    public LSHFunction(int dimensionD, int dimensionK, long randomSeed) {
        if (dimensionD <= 0) throw new IllegalArgumentException("dimensionD must be positive");
        if (dimensionK <= 0) throw new IllegalArgumentException("dimensionK must be positive");

        this.dimensionK = dimensionK;
        this.dimensionD = dimensionD;
        this.randomSeed = randomSeed;
    }

    /**
     * Implement Hash Function
     *
     * @param vectorD the input vector in {0,1}^d
     * @return vectorK the compressed input vector in {0,1}^k
     */
    public String computeHash(int[] vectorD) {
        // 1. Generate vector K from K dimension
        int[] randomIndexK = new int[dimensionK];
        for (var k = 0; k < dimensionK; k++) {
            randomIndexK[k] = new Random(randomSeed + k).nextInt(dimensionD);
        }

        // 2. Compress vector D to K dimension
        var sb = new StringBuilder();
        for (int kIndex : randomIndexK) {
            sb.append(vectorD[kIndex]);
        }

        return sb.toString();
    }

}
