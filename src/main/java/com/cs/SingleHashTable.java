package com.cs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleHashTable {

    private final LSHFunction lshFunction;
    private final Map<String, List<int[]>> hashTable;

    public SingleHashTable(int dimensionD, int dimensionK, long randomSeed) {
        if (dimensionD <= 0) throw new IllegalArgumentException("dimensionD must be positive");
        if (dimensionK <= 0) throw new IllegalArgumentException("dimensionK must be positive");

        this.lshFunction = new LSHFunction(dimensionD, dimensionK, randomSeed);
        this.hashTable = new HashMap<>();
    }

    public void insert(int[] vector) {
        String hashValue = lshFunction.computeHash(vector);
        hashTable.computeIfAbsent(hashValue, k -> new ArrayList<>()).add(vector);
    }

    public void insertAll(int[][] dataset) {
        for (int[] vectorD : dataset) {
            this.insert(vectorD);
        }
    }

    public void remove(int[] vector) {
        String hashValue = lshFunction.computeHash(vector);
        List<int[]> chainBucket = hashTable.get(hashValue);
        if (chainBucket != null) hashTable.remove(hashValue);
    }

    public void removeAll(int[][] dataset) {
        for (int[] vectorD : dataset) {
            this.remove(vectorD);
        }
    }

    public List<int[]> query(int[] vector) {
        String hashValue = lshFunction.computeHash(vector);
        return hashTable.getOrDefault(hashValue, Collections.emptyList());
    }

    public Map<String, List<int[]>> getHashTable() {
        return this.hashTable;
    }

}
