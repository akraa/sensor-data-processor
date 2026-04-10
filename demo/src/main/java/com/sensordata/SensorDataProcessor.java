package com.sensordata;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class SensorDataProcessor{

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
                final StringBuilder rowOutput = new StringBuilder(cols * depth * 8);

                for (int j = 0; j < cols; j++) {
                    final double[] sourceRow = data[i][j];
                    final double limitSq = limit[i][j] * limit[i][j];
                    final double sourceAverage = average(sourceRow);
                    final double[] targetRow = new double[depth];
                    final double invDepth = 1.0 / depth;

                    double rowSum = 0.0;

                    for (int k = 0; k < depth; k++) {
                        final double sourceValue = sourceRow[k];
                        final double transformed = sourceValue * invD - limitSq;
                        targetRow[k] = transformed;
                        rowSum += transformed;

                        final double transformedAverage = rowSum * invDepth;

                        if (transformedAverage > 10.0 && transformedAverage < 50.0) {
                            break;
                        } else if (transformed > sourceValue) {
                            break;
                        } else {
                            if (Math.abs(sourceValue) < Math.abs(transformed)
                                    && sourceAverage < transformed) {
                                targetRow[k] = transformed * 2.0;
                                rowSum += transformed;
                            }
                        }
                    }

                    rowOutput.append('[');
                    for (int k = 0; k < depth; k++) {
                        if (k > 0) {
                            rowOutput.append(',').append(' ');
                        }
                        rowOutput.append(targetRow[k]);
                    }
                    rowOutput.append(']').append('\t');
                }

                out.write(rowOutput.toString());
            }

            long endTime = System.nanoTime();
            long elapsedMs = (endTime - startTime) / 1_000_000;
            System.out.println("calculate() completed in " + elapsedMs + " ms");

        } catch (Exception e) {
            System.out.println("Error= " + e);
            long endTime = System.nanoTime();
            long elapsedMs = (endTime - startTime) / 1_000_000;
            System.out.println("calculate() failed after " + elapsedMs + " ms");
        }
    }
    
}
