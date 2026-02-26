package com.cs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Main {

    private final DatasetGenerator datasetGenerator;
    private final int dimensionD;
    private final int dimensionK;
    private final long randomSeed;

    public Main(int dataPointN, int dimensionD, int dimensionK, long randomSeed) {
        if (dimensionD <= 0) throw new IllegalArgumentException("dimensionD must be positive");
        if (dimensionK <= 0) throw new IllegalArgumentException("dimensionK must be positive");

        this.dimensionD = dimensionD;
        this.dimensionK = dimensionK;
        this.randomSeed = randomSeed;
        this.datasetGenerator = new DatasetGenerator(dataPointN, dimensionD, randomSeed);
    }

    public void run(int independentRuns) {
        // 1. Data Generation
        int[][] dataset = datasetGenerator.dataGeneration();
        System.out.println("Generated Dataset: " + Arrays.deepToString(dataset));

        // 2. Implement multiple hash table
        var hashTables = new ArrayList<SingleHashTable>();
        for (var i = 0; i < independentRuns; i++) {
            // 2.1. Declare single hash table
            var singleHashTable = new SingleHashTable(this.dimensionD, this.dimensionK, this.randomSeed);

            // 2.2. Insert dataset into hash table
            singleHashTable.insertAll(dataset);

            // 2.3. Add hash table to the list of hash tables
            hashTables.add(singleHashTable);
        }

        // 3. Randomly select value from dataset
        var random = new Random(this.randomSeed);
        var queryKey = dataset[random.nextInt(datasetGenerator.getDataPoint())];

        // 4. Query the hash tables and collect candidate vectors
        var candidateVectors = new HashSet<int[]>();
        for (var hashTable : hashTables) {
            candidateVectors.addAll(hashTable.query(queryKey));
        }
        System.out.println("Candidate Vectors: " + candidateVectors);
    }

}
