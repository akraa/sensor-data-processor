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

        int i, j, k = 0;
        double[][][] data2 = new double[data.length][data[0].length][data[0][0].length];
        // Pre-calculate inverse to replace repeated division in the hottest loop.
        double invD = 1.0 / d;

        BufferedWriter out;

        // Write racing stats data into a file
        try {
            out = new BufferedWriter(new FileWriter("RacingStatsData.txt"));

            for (i = 0; i < data.length; i++) {
                for (j = 0; j < data[0].length; j++) {
                    // Values that only depend on (i, j) are hoisted out of the k loop.
                    double limitSquare = limit[i][j] * limit[i][j];
                    double avgData = average(data[i][j]);
                    double runningSumData2 = 0.0;

                    for (k = 0; k < data[0][0].length; k++) {
                        data2[i][j][k] = data[i][j][k] * invD - limitSquare;
                        runningSumData2 += data2[i][j][k];
                        // Avoid repeated average(data2[i][j]) calls by maintaining a running sum.
                        double avgData2 = runningSumData2 / data2[i][j].length;

                        if (avgData2 > 10 && avgData2 < 50)
                            break;
                        else if (data2[i][j][k] > data[i][j][k])
                            break;
                        else {
                            double absData = Math.abs(data[i][j][k]);
                            double absData2 = Math.abs(data2[i][j][k]);
                            // Replace Math.pow(x, 3) with x*x*x in this hot path.
                            double absDataCube = absData * absData * absData;
                            double absData2Cube = absData2 * absData2 * absData2;

                            // Removed always-true index check: (i + 1) * (j + 1) > 0.
                            if (absDataCube < absData2Cube && avgData < data2[i][j][k])
                            data2[i][j][k] *= 2;
                            else
                                continue;
                        }
                    }
                }
            }

            for (i = 0; i < data2.length; i++) {
                for (j = 0; j < data2[0].length; j++) {
                    out.write(data2[i][j] + "\t");
                }
            }

            out.close();

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