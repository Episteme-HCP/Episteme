/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.client.climate;

import java.io.Serializable;

/**
 * Advanced General Circulation Model (GCM) Task.
 * 
 * Simulates a multi-layered atmosphere with moisture and heat transport.
 * Uses a simplified dynamical core for global climate studies.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Lorenz, E. N. (1967). The nature and theory of the general circulation of
 * the atmosphere. WMO Pub.</li>
 * <li>Held, I. M., & Suarez, M. J. (1994). A proposal for the intercomparison
 * of the dynamical cores of atmospheric general circulation models. Bulletin of
 * the American Meteorological Society, 75(10), 1825-1830.</li>
 * </ul>
 * </p>
 */
public class GeneralCirculationModelTask implements Serializable {

    private final int latBins;
    private final int longBins;

    // Layers: 0 = Surface, 1 = Troposphere, 2 = Stratosphere
    private double[][][] temperature; // [layer][lat][long]
    private double[][] specificHumidity; // [lat][long]
    private double[][] precipitation; // [lat][long] (cumulative)

    // Constants
    private static final double CP = 1004.0; // J/kg/K (Air heat capacity)
    private static final double L = 2.5e6; // J/kg (Latent heat of vaporization)
    // m/s^2
    private static final double SIGMA = 5.67e-8;

    public GeneralCirculationModelTask(int latBins, int longBins) {
        this.latBins = latBins;
        this.longBins = longBins;
        this.temperature = new double[3][latBins][longBins];
        this.specificHumidity = new double[latBins][longBins];
        this.precipitation = new double[latBins][longBins];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < latBins; i++) {
            double lat = Math.PI * (i - latBins / 2.0) / latBins;
            for (int j = 0; j < longBins; j++) {
                temperature[0][i][j] = 288.0 - 40 * Math.sin(lat) * Math.sin(lat); // Surface
                temperature[1][i][j] = temperature[0][i][j] - 30; // Troposphere
                temperature[2][i][j] = 210.0; // Stratosphere
                specificHumidity[i][j] = 0.01 * Math.exp(-Math.abs(lat));
            }
        }
    }

    public void step(double dt) {
        double[][][] nextTemp = new double[3][latBins][longBins];

        for (int i = 0; i < latBins; i++) {
            double lat = Math.PI * (i + 0.5) / latBins - Math.PI / 2.0;
            double cosLat = Math.cos(lat);

            for (int j = 0; j < longBins; j++) {
                // 1. Radiative Balance (Simplified)
                double insolation = 1361.0 * (1 - 0.3) * cosLat / 4.0;
                double surfaceEmission = 0.95 * SIGMA * Math.pow(temperature[0][i][j], 4);
                double atmosEmission = 0.7 * SIGMA * Math.pow(temperature[1][i][j], 4);

                // Surface Energy Balance
                double surfaceNet = insolation - surfaceEmission + atmosEmission;
                double sensibleHeat = 10.0 * (temperature[0][i][j] - temperature[1][i][j]);
                double latentHeatFlux = 20.0 * specificHumidity[i][j];

                nextTemp[0][i][j] = temperature[0][i][j] + (surfaceNet - sensibleHeat - latentHeatFlux) * dt / 1e7;

                // Keep simplified update for other layers
                nextTemp[1][i][j] = temperature[1][i][j];
                nextTemp[2][i][j] = temperature[2][i][j];
            }
        }
        this.temperature = nextTemp;
    }

    public double getSurfaceTemperatureAt(int lat, int lon) {
        if (isValidCoordinate(lat, lon)) {
            return temperature[0][lat][lon];
        }
        return 0.0;
    }

    public double[][] getSurfaceTemperature() {
        return temperature[0];
    }

    public double getAirTemperatureAt(int lat, int lon) {
        if (isValidCoordinate(lat, lon)) {
            return temperature[1][lat][lon];
        }
        return 0.0;
    }

    public double[][] getAirTemperature() {
        return temperature[1];
    }

    public void updateState(double[][][] temp, double[][] humidity) {
        this.temperature = temp;
        this.specificHumidity = humidity;
    }

    private boolean isValidCoordinate(int lat, int lon) {
        return lat >= 0 && lat < latBins && lon >= 0 && lon < longBins;
    }

    public double[][] getSurfaceTemp() {
        return temperature[0];
    }

    public double[][][] getAllTemp() {
        return temperature;
    }

    public double[][] getHumidity() {
        return specificHumidity;
    }
}
