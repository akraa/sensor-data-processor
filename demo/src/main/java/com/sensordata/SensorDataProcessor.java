package com.sensordata;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class SensorDataProcessor{
    private static final boolean LOG_TIMING = false;

    // Senson data and limits.
    public double[][][] data;
    public double[][] limit;

    // constructor
    public SensorDataProcessor(double[][][] data, double[][] limit) {
        this.data = data;
        this.limit = limit;
    }

    // calculate data
    public void calculate(double d) {

        final long startTime = LOG_TIMING ? System.nanoTime() : 0L;

        final int rows = data.length;
        final int cols = data[0].length;
        final int depth = data[0][0].length;
        final double invD = 1.0 / d;
        final double invDepth = 1.0 / depth;

        // Write racing stats data into a file
        try (BufferedWriter out = new BufferedWriter(new FileWriter("RacingStatsData.txt"))) {

            for (int i = 0; i < rows; i++) {
                final double[][] dataI = data[i];
                final double[] limitI = limit[i];

                for (int j = 0; j < cols; j++) {
                    final double[] sourceRow = dataI[j];
                    final double limitValue = limitI[j];
                    final double limitSq = limitValue * limitValue;
                    double sourceSum = 0.0;
                    for (int k = 0; k < depth; k++) {
                        sourceSum += sourceRow[k];
                    }
                    final double sourceAverage = sourceSum * invDepth;

                    double rowSum = 0.0;
                    int nextIndex = 0;

                    out.write('[');

                    for (int k = 0; k < depth; k++) {
                        final double sourceValue = sourceRow[k];
                        final double transformed = sourceValue * invD - limitSq;
                        double outputValue = transformed;
                        rowSum += outputValue;

                        final double transformedAverage = rowSum * invDepth;

                        boolean shouldBreak = false;
                        if (transformedAverage > 10.0 && transformedAverage < 50.0) {
                            shouldBreak = true;
                        } else if (transformed > sourceValue) {
                            shouldBreak = true;
                        } else if (Math.abs(sourceValue) < Math.abs(transformed)
                                && sourceAverage < transformed) {
                            outputValue = transformed * 2.0;
                            rowSum += transformed;
                        }

                        if (k > 0) {
                            out.write(", ");
                        }
                        out.write(Double.toString(outputValue));
                        nextIndex = k + 1;

                        if (shouldBreak) {
                            break;
                        }
                    }

                    for (int k = nextIndex; k < depth; k++) {
                        out.write(", 0.0");
                    }
                    out.write("]\t");
                }

            }

            if (LOG_TIMING) {
                long endTime = System.nanoTime();
                long elapsedMs = (endTime - startTime) / 1_000_000;
                System.out.println("calculate() completed in " + elapsedMs + " ms");
            }

        } catch (Exception e) {
            System.out.println("Error= " + e);
            if (LOG_TIMING) {
                long endTime = System.nanoTime();
                long elapsedMs = (endTime - startTime) / 1_000_000;
                System.out.println("calculate() failed after " + elapsedMs + " ms");
            }
        }
    }
    
}
