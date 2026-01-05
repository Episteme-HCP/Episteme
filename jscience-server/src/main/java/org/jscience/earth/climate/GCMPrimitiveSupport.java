/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.earth.climate;

/**
 * Primitive-based support for General Circulation Model (GCM) simulation.
 * Optimized for high-performance double-precision computation.
 */
public class GCMPrimitiveSupport {

    private static final double SIGMA = 5.67e-8;

    /**
     * Solves one time step of the GCM using double primitives.
     * 
     * @param temperature      Temperature field [layer][lat][long]
     * @param specificHumidity Humidity field [lat][long]
     * @param u                Zonal wind [layer][lat][long]
     * @param v                Meridional wind [layer][lat][long]
     * @param w                Vertical wind [layer][lat][long]
     * @param dt               Time step
     * @param latBins          Number of latitude bins
     * @param longBins         Number of longitude bins
     */
    public void step(double[][][] temperature, double[][] specificHumidity,
            double[][][] u, double[][][] v, double[][][] w,
            double dt, int latBins, int longBins) {

        // 1. Fluid Dynamics (Simplified Placeholder for Primitive Mode)
        // In a real scenario, this would call an optimized LBM or NS solver for
        // doubles.
        // For this refactor, we maintain the GCM logic structure.

        // 2. Radiative & Thermodynamic Calculation
        double[][][] nextTemp = new double[3][latBins][longBins];

        for (int i = 0; i < latBins; i++) {
            double lat = Math.PI * (i + 0.5) / latBins - Math.PI / 2.0;
            double cosLat = Math.cos(lat);

            for (int j = 0; j < longBins; j++) {
                // Radiative Balance (Simplified)
                double insolation = 1361.0 * (1 - 0.3) * cosLat / 4.0;
                double surfaceEmission = 0.95 * SIGMA * Math.pow(temperature[0][i][j], 4);
                double atmosEmission = 0.7 * SIGMA * Math.pow(temperature[1][i][j], 4);

                // Surface Energy Balance
                double surfaceNet = insolation - surfaceEmission + atmosEmission;
                double sensibleHeat = 10.0 * (temperature[0][i][j] - temperature[1][i][j]);
                double latentHeatFlux = 20.0 * specificHumidity[i][j];

                nextTemp[0][i][j] = temperature[0][i][j] + (surfaceNet - sensibleHeat - latentHeatFlux) * dt / 1e7;
                nextTemp[1][i][j] = temperature[1][i][j];
                nextTemp[2][i][j] = temperature[2][i][j];

                // Simple Advection (Zonal wind only for this simplified support)
                int prevJ = (j - 1 + longBins) % longBins;
                double advection = u[1][i][j] * (temperature[1][i][j] - temperature[1][i][prevJ]) * dt / 1e5;
                nextTemp[1][i][j] -= advection;
            }
        }

        // Copy back to temperature array
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                System.arraycopy(nextTemp[k][i], 0, temperature[k][i], 0, longBins);
            }
        }
    }
}
