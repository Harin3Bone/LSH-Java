package com.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

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
        log.info("Generated dataset with n = {}, with d = {}", datasetGenerator.getDataPoint(), this.dimensionD);
        log.info("Experiment with k = {} and L = {}", this.dimensionK, independentRuns);

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
        log.info("Generated {} independent hash tables", independentRuns);

        // 3. Randomly select value from dataset
        var random = new Random(this.randomSeed);
        var queryKey = dataset[random.nextInt(datasetGenerator.getDataPoint())];
        log.info("Random query vector: {}", Arrays.toString(queryKey));

        // 4. Query the hash tables and collect candidate vectors
        var candidateVectors = new HashSet<int[]>();
        for (var hashTable : hashTables) {
            candidateVectors.addAll(hashTable.query(queryKey));
        }
        log.info("Candidate Vectors size: {}", candidateVectors.size());
        log.info("Candidate Vectors: {}", candidateVectors);
    }

}
