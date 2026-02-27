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
    private final int independentRunsL;
    private final long randomSeed;

    public Main(int dataPointN, int dimensionD, int dimensionK, int independentRunsL, long randomSeed) {
        if (dimensionD <= 0) throw new IllegalArgumentException("dimensionD must be positive");
        if (dimensionK <= 0) throw new IllegalArgumentException("dimensionK must be positive");

        this.dataPointN = dataPointN;
        this.dimensionD = dimensionD;
        this.dimensionK = dimensionK;
        this.independentRunsL = independentRunsL;
        this.randomSeed = randomSeed;
        this.datasetGenerator = new DatasetGenerator(dataPointN, dimensionD, randomSeed);
    }

    public void run() {
        // 1. Data Generation
        int[][] dataset = datasetGenerator.dataGeneration();
        log.info("Generated dataset with n = {}, with d = {}", this.dataPointN, this.dimensionD);
        log.info("Experiment with k = {} and L = {}", this.dimensionK, independentRunsL);

        // 2. Implement multiple hash table
        var hashTables = new ArrayList<SingleHashTable>();
        for (var i = 0; i < independentRunsL; i++) {
            // 2.1. Declare single hash table
            var singleHashTable = new SingleHashTable(this.dimensionD, this.dimensionK, this.randomSeed);

            // 2.2. Insert dataset into hash table
            singleHashTable.insertAll(dataset);

            // 2.3. Add hash table to the list of hash tables
            hashTables.add(singleHashTable);
        }
        log.info("Generated {} independent hash tables", independentRunsL);

        // 3. Begin query processing
        var random = new Random(this.randomSeed);
        var queryKeys = new int[FIXED_NUM_QUERIES][this.dimensionD];
        for (int i = 0; i < FIXED_NUM_QUERIES; i++) {
            queryKeys[i] = dataset[random.nextInt(this.dataPointN)];
        }
        log.info("Random query vector: {}", Arrays.toString(queryKeys));

        // 4. Evaluate performance
        EvaluateResult result = getEvaluatePerformance(queryKeys, hashTables);
        log.info("Average iterations per iteration = {}", result.getAvgIteration());
        log.info("Success rate = {}%", result.getSuccessRate());
    }

    private EvaluateResult getEvaluatePerformance(int[][] queryKeys, ArrayList<SingleHashTable> hashTables) {
        var totalIterations = 0L;
        var successCount = 0;
        var queryIteration = 0;

        // 1. Run iteration for each query key to begin evaluate performance
        for (int[] queryKey : queryKeys) {
            // 1.1. Get candidate vectors from all SingleHashTable
            var candidateVectors = new HashSet<int[]>();
            for (SingleHashTable table : hashTables) {
                candidateVectors.addAll(table.query(queryKey));
            }
            log.info("Iteration {} Candidate vectors size: {}", ++queryIteration, candidateVectors.size());


            // 1.2. Count iteration until find first vector <= ANN threshold
            int currentIterations = 0;
            boolean found = false;
            for (int[] candidateVector : candidateVectors) {
                currentIterations++;
                if (calcHammingDistance(queryKey, candidateVector) <= THRESHOLD) {
                    found = true;
                    break;
                }
            }

            // 1.3. Update total iteration
            totalIterations += currentIterations;

            // 1.4. Update success count if query value exists in candidate vector
            if (found) {
                successCount++;
            }
        }

        return new EvaluateResult(totalIterations, successCount);
    }

    /**
     * Calculate Hamming distance between two vectors
     *
     * @param vectorD the input query vector in {0,1}^d
     * @param vectorK the candidate vector from all SingleHashTable in {0,1}^k
     * @return Hamming distance between vectorD and vectorK
     */
    private int calcHammingDistance(int[] vectorD, int[] vectorK) {
        int distance = 0;
        for (int i = 0; i < vectorD.length; i++) {
            if (vectorD[i] != vectorK[i]) {
                distance++;
            }
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
