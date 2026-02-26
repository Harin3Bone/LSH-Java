package com.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    // Threshold for hamming space (c = 2, r = 10) - ANN
    private static final int THRESHOLD = 2 * 10;
    private static final int FIXED_NUM_QUERIES = 10;

    private final DatasetGenerator datasetGenerator;
    private final int dataPointN;
    private final int dimensionD;
    private final int dimensionK;
    private final long randomSeed;

    public Main(int dataPointN, int dimensionD, int dimensionK, long randomSeed) {
        if (dimensionD <= 0) throw new IllegalArgumentException("dimensionD must be positive");
        if (dimensionK <= 0) throw new IllegalArgumentException("dimensionK must be positive");

        this.dataPointN = dataPointN;
        this.dimensionD = dimensionD;
        this.dimensionK = dimensionK;
        this.randomSeed = randomSeed;
        this.datasetGenerator = new DatasetGenerator(dataPointN, dimensionD, randomSeed);
    }

    public void run(int independentRuns) {
        // 1. Data Generation
        int[][] dataset = datasetGenerator.dataGeneration();
        log.info("Generated dataset with n = {}, with d = {}", this.dataPointN, this.dimensionD);
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

        // 3. Begin query processing
        var random = new Random(this.randomSeed);
        var queryKey = new int[FIXED_NUM_QUERIES][this.dimensionD];
        for (int i = 0; i < FIXED_NUM_QUERIES; i++) {
            queryKey[i] = dataset[random.nextInt(this.dataPointN)];
        }
        log.info("Random query vector: {}", Arrays.toString(queryKey));

        // 4. Evaluate performance
        EvaluateResult result = getEvaluatePerformance(queryKey, hashTables);
        log.info("Average iterations per iteration = {}", result.getAvgIteration());
        log.info("Success rate = {}%", result.getSuccessRate());
    }

    private EvaluateResult getEvaluatePerformance(int[][] queryKey, ArrayList<SingleHashTable> hashTables) {
        var totalIterations = 0L;
        var successCount = 0;
        var queryIteration = 0;
        for (int[] query : queryKey) {
            var candidateVectors = new HashSet<int[]>();
            for (SingleHashTable table : hashTables) {
                candidateVectors.addAll(table.query(query));
            }
            log.info("Iteration {} Candidate vectors size: {}", ++queryIteration, candidateVectors.size());

            int currentIterations = 0;
            boolean found = false;

            // Count iteration until find first vector <= ANN threshold
            for (int[] p : candidateVectors) {
                currentIterations++;
                if (calcHammingDistance(query, p) <= THRESHOLD) {
                    found = true;
                    break;
                }
            }

            totalIterations += currentIterations;
            if (found) successCount++;
        }

        return new EvaluateResult(totalIterations, successCount);
    }


    private int calcHammingDistance(int[] vectorD, int[] vectorK) {
        int distance = 0;
        for (int i = 0; i < vectorD.length; i++) {
            if (vectorD[i] != vectorK[i]) distance++;
        }
        return distance;
    }


    private record EvaluateResult(double totalIteration, double successCount) {
        double getAvgIteration() {
            return totalIteration / FIXED_NUM_QUERIES;
        }

        double getSuccessRate() {
            return (successCount * 100.0) / FIXED_NUM_QUERIES;
        }
    }

}
