/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.apps.physics.spintronics;

import javafx.scene.chart.XYChart;
import java.io.*;
import java.util.List;

/**
 * Data export utilities for spintronic simulation results.
 */
public class DataExporter {

    /**
     * Exports XYChart series to CSV format.
     */
    @SafeVarargs
    public static void exportToCSV(String filename, XYChart.Series<Number, Number>... seriesList) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Header
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < seriesList.length; i++) {
                String name = seriesList[i].getName() != null ? seriesList[i].getName() : "Series" + i;
                header.append(name).append("_X,").append(name).append("_Y");
                if (i < seriesList.length - 1) header.append(",");
            }
            writer.println(header);

            // Find max data points
            int maxPoints = 0;
            for (XYChart.Series<Number, Number> s : seriesList) {
                maxPoints = Math.max(maxPoints, s.getData().size());
            }

            // Data rows
            for (int row = 0; row < maxPoints; row++) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < seriesList.length; i++) {
                    if (row < seriesList[i].getData().size()) {
                        XYChart.Data<Number, Number> d = seriesList[i].getData().get(row);
                        line.append(d.getXValue()).append(",").append(d.getYValue());
                    } else {
                        line.append(",");
                    }
                    if (i < seriesList.length - 1) line.append(",");
                }
                writer.println(line);
            }
        }
    }

    /**
     * Exports magnetization trajectory to CSV.
     */
    public static void exportTrajectory(String filename, List<double[]> trajectory) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Time_ps,Mx,My,Mz");
            int t = 0;
            for (double[] m : trajectory) {
                writer.printf("%d,%.6f,%.6f,%.6f%n", t++, m[0], m[1], m[2]);
            }
        }
    }

    /**
     * Exports 1D micromagnetic state to CSV.
     */
    public static void exportMicromagnetics1D(String filename, Micromagnetics1D sim) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Cell,X_nm,Mx,My,Mz");
            for (int i = 0; i < sim.getNumCells(); i++) {
                double x = i * sim.getCellSize().doubleValue() * 1e9;
                org.jscience.mathematics.numbers.real.Real[] m = sim.getMagnetization(i);
                writer.printf("%d,%.2f,%.6f,%.6f,%.6f%n", i, x, m[0].doubleValue(), m[1].doubleValue(), m[2].doubleValue());
            }
        }
    }

    /**
     * Exports power spectrum to CSV.
     */
    public static void exportSpectrum(String filename, double[] frequencies, org.jscience.mathematics.numbers.real.Real[] psd) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Frequency_GHz,Power");
            int len = Math.min(frequencies.length, psd.length);
            for (int i = 0; i < len; i++) {
                writer.printf("%.4f,%.6e%n", frequencies[i] / 1e9, psd[i].doubleValue());
            }
        }
    }
}
