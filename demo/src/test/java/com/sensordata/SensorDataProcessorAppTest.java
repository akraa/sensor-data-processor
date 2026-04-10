package com.sensordata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SensorDataProcessorApp Test Suite")
public class SensorDataProcessorAppTest {
    private static final String OUTPUT_FILE = "RacingStatsData.txt";

    @BeforeEach
    public void setUp() {
        File file = new File(OUTPUT_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("App class can be instantiated")
    public void testConstructorCoverage() {
        SensorDataProcessorApp app = new SensorDataProcessorApp();
        assertNotNull(app);
    }

    @Test
    @DisplayName("Main runs successfully and produces output file")
    public void testMainCoverage() {
        assertDoesNotThrow(() -> SensorDataProcessorApp.main(new String[0]));
        File file = new File(OUTPUT_FILE);
        assertTrue(file.exists(), "Output file should be created by main");
        assertTrue(file.length() > 0, "Output file should contain data");
    }
}
