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

    // calculates average of sensor data
    private double average(double[] array) {
        int i = 0;
        double val = 0;
        for (i = 0; i < array.length; i++) {
            val += array[i];
        }

        return val / array.length;
    }

    // calculate data
    public void calculate(double d) {

        long startTime = System.nanoTime();

        final int rows = data.length;
        final int cols = data[0].length;
        final int depth = data[0][0].length;
        final double invD = 1.0 / d;

        // Write racing stats data into a file
        try (BufferedWriter out = new BufferedWriter(new FileWriter("RacingStatsData.txt"))) {

            for (int i = 0; i < rows; i++) {
                final double[][] dataI = data[i];
                final double[] limitI = limit[i];

                for (int j = 0; j < cols; j++) {
                    final double[] sourceRow = dataI[j];
                    final double limitValue = limitI[j];
                    final double limitSq = limitValue * limitValue;
                    final double sourceAverage = average(sourceRow);
                    final double invDepth = 1.0 / depth;

                    double rowSum = 0.0;
                    int nextIndex = 0;

                    out.write('[');

                    for (int k = 0; k < depth; k++) {
                        final double sourceValue = sourceRow[k];
                        final double transformed = sourceValue * invD - limitSq;
                        double outputValue = transformed;
                        rowSum += outputValue;

                        final double transformedAverage = rowSum * invDepth;

                        if (Math.abs(sourceValue) < Math.abs(transformed)
                                && sourceAverage < transformed) {
                            outputValue = transformed * 2.0;
                            rowSum += transformed;
                        }

                        if (k > 0) {
                            out.write(", ");
                        }
                        out.write(Double.toString(outputValue));
                        nextIndex = k + 1;

                        if (transformedAverage > 10.0 && transformedAverage < 50.0) {
                            break;
                        } else if (transformed > sourceValue) {
                            break;
                        }
                    }

                    for (int k = nextIndex; k < depth; k++) {
                        if (k > 0) {
                            out.write(", ");
                        }
                        out.write("0.0");
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
