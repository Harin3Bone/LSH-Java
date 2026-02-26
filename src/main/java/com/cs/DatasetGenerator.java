package com.cs;

import java.util.Random;

public class DatasetGenerator {

    private final int dataPoint;
    private final int dimension;
    private final long randomSeed;

    public DatasetGenerator(int dataPoint, int dimension, long randomSeed) {
        if (dataPoint <= 0) throw new IllegalArgumentException("dataPoint must be positive");
        if (dimension <= 0) throw new IllegalArgumentException("dimension must be positive");

        this.dataPoint = dataPoint;
        this.dimension = dimension;
        this.randomSeed = randomSeed;
    }

    public int[][] dataGeneration() {
        // 1. Define fixed size 2D array
        var dataSet = new int[this.dataPoint][this.dimension];

        // 2. Define random generator with fixed seed for reproduce
        var random = new Random(randomSeed);

        // 3. Fill the array with random binary values (0 or 1)
        for (int i = 0; i < this.dataPoint; i++) {
            for (int j = 0; j < this.dimension; j++) {
                dataSet[i][j] = random.nextInt(2);
            }
        }

        // 4. Return dataset
        return dataSet;
    }

}
