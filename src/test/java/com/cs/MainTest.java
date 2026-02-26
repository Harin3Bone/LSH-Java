package com.cs;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MainTest {

    private static final long RANDOM_SEED = 12345L;

    @ParameterizedTest
    @CsvSource(value = {
            "10, 50",
            "50, 2",
            "100, 1"
    })
    void testDataSetX(int dimensionK, int L) {
        // Given
        var dataPointN = 10000;
        var dimensionD = 100;

        // When
        var main = new Main(dataPointN, dimensionD, dimensionK, RANDOM_SEED);
        main.run(L);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 5",
            "10, 5",
            "20, 2",
    })
    void testDatasetQ(int dimensionK, int L) {
        // Given
        var dataPointN = 100;
        var dimensionD = 100;

        // When
        var main = new Main(dataPointN, dimensionD, dimensionK, RANDOM_SEED);
        main.run(L);
    }

}
