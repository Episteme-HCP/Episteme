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

package org.jscience.earth.climate;

import java.io.Serializable;

/**
 * Climate Model Simulation Task.
 * 
 * Simulates heat distribution on a sphere (simplified Energy Balance Model).
 */
public class ClimateModelTask implements Serializable {

    private final int latitudeBins;
    private final int longitudeBins;
    private double[][] temperature; // [lat][long] in Kelvin

    // Physical Constants
    private static final double SOLAR_CONSTANT = 1361.0; // W/m^2
    private static final double ALBEDO = 0.3;
    private static final double EMISSIVITY = 0.6;
    private static final double SIGMA = 5.67e-8; // Stefan-Boltzmann constant

    public ClimateModelTask(int latBins, int longBins) {
        this.latitudeBins = latBins;
        this.longitudeBins = longBins;
        this.temperature = new double[latBins][longBins];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < latitudeBins; i++) {
            for (int j = 0; j < longitudeBins; j++) {
                // Initial guess: 288K with some gradient
                double lat = Math.PI * (i - latitudeBins / 2.0) / latitudeBins;
                temperature[i][j] = 288.0 - 50 * Math.abs(Math.sin(lat));
            }
        }
    }

    public void runStep(double dt) {
        double[][] nextTemp = new double[latitudeBins][longitudeBins];
        double s0 = SOLAR_CONSTANT;
        double a = ALBEDO;
        double eps = EMISSIVITY;

        for (int i = 0; i < latitudeBins; i++) {
            double lat = Math.PI * (i + 0.5) / latitudeBins - Math.PI / 2.0;

            for (int j = 0; j < longitudeBins; j++) {
                // 1. Incoming Solar Radiation
                double insolation = s0 * (1 - a) * Math.cos(lat) / 4.0;

                // 2. Outgoing Longwave Radiation
                double olr = eps * SIGMA * Math.pow(temperature[i][j], 4);

                // 3. Diffusion (Heat transport)
                double diffusion = 0.0;
                int west = (j - 1 + longitudeBins) % longitudeBins;
                int east = (j + 1) % longitudeBins;
                diffusion += temperature[i][west] - temperature[i][j];
                diffusion += temperature[i][east] - temperature[i][j];

                double flux = diffusion * 0.1;

                // Energy Balance
                double heatCapacity = 10e6; // J/m^2/K
                double rate = (insolation - olr + flux * 100) / heatCapacity;

                nextTemp[i][j] = temperature[i][j] + rate * dt;
            }
        }
        temperature = nextTemp;
    }

    public double[][] getTemperatureMap() {
        return temperature;
    }

    public int getLatitudeBins() {
        return latitudeBins;
    }

    public int getLongitudeBins() {
        return longitudeBins;
    }
}
