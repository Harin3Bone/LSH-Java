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
    void testDataSetX(int k, int L) {
        // Given
        var n = 10000;
        var d = 100;

        // When
        var main = new Main(n, d, k, L, RANDOM_SEED);
        main.run();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 5",
            "10, 5",
            "20, 2",
    })
    void testDatasetQ(int dimensionK, int L) {
        // Given
        var n = 100;
        var d = 100;

        // When
        var main = new Main(n, d, dimensionK, L, RANDOM_SEED);
        main.run();
    }

}
