package com.cs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    private final DatasetGenerator datasetGenerator;
    private final SingleHashTable singleHashTable;

    public Main(DatasetGenerator datasetGenerator, SingleHashTable singleHashTable) {
        this.datasetGenerator = datasetGenerator;
        this.singleHashTable = singleHashTable;
    }

    public void run(int independentRuns) {
        // 1. Data Generation
        int[][] dataSet = datasetGenerator.dataGeneration();
        System.out.println("Generated Dataset: " + Arrays.deepToString(dataSet));

        // 2. Implement multiple hash table
        for (var i = 0; i < independentRuns; i++) {
            singleHashTable.insertAll(dataSet);
            Map<String, List<int[]>> hashTable = singleHashTable.getHashTable();
            System.out.println("Independent Hash Table " + i + ": " + hashTable);
        }

        // 3. Optimizing parameters k and l
    }

}
