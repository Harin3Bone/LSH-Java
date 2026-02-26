package com.cs;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MainTest {

    private static final long RANDOM_SEED = 12345L;

    @ParameterizedTest(name = "scenario {index}: {0} with n = {1} and d = {2}")
    @CsvSource(value = {
            "Dataset X, 10000, 100",
            "Dataset Q, 100, 100"
    }, delimiter = ',')
    void testDataSet(String scenarioName, int dataPointN, int dimensionD) {
        // Given
        var dimensionK = 10;
        var L = 2;

        // When
        var main = new Main(dataPointN, dimensionD, dimensionK, RANDOM_SEED);
        main.run(L);
    }

}
