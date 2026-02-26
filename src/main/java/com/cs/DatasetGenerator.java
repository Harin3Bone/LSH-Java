package com.cs;

import java.util.Random;

public class DatasetGenerator {

    private final int dataPoint;
    private final int dimensionD;
    private final long randomSeed;

    public DatasetGenerator(int dataPoint, int dimensionD, long randomSeed) {
        if (dataPoint <= 0) throw new IllegalArgumentException("dataPoint must be positive");
        if (dimensionD <= 0) throw new IllegalArgumentException("dimension must be positive");

        this.dataPoint = dataPoint;
        this.dimensionD = dimensionD;
        this.randomSeed = randomSeed;
    }

    public int getDataPoint() {
        return dataPoint;
    }

    /**
     * Generate dataset of N data points in D dimension
     *
     * @return vector {0,1}^d of size N
     */
    public int[][] dataGeneration() {
        // 1. Define fixed size 2D array
        var dataSet = new int[this.dataPoint][this.dimensionD];

        // 2. Define random generator with fixed seed for reproduce
        var random = new Random(randomSeed);

        // 3. Fill the array with random binary values {0,1}^d
        for (int i = 0; i < this.dataPoint; i++) {
            for (int j = 0; j < this.dimensionD; j++) {
                dataSet[i][j] = random.nextInt(2);
            }
        }

        // 4. Return dataset
        return dataSet;
    }

}
