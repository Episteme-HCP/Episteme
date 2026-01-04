/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.earth.climate;

import org.jscience.distributed.DistributedTask;
import java.io.Serializable;

/**
 * Advanced General Circulation Model (GCM) Task.
 */
public class GeneralCirculationModelTask
        implements DistributedTask<GeneralCirculationModelTask, GeneralCirculationModelTask>, Serializable {

    private final int latBins;
    private final int longBins;

    // Layers: 0 = Surface, 1 = Troposphere, 2 = Stratosphere
    private double[][][] temperature; // [layer][lat][long]
    private double[][] specificHumidity; // [lat][long]
    private double[][] precipitation; // [lat][long] (cumulative)

    // Constants
    private static final double CP = 1004.0; // J/kg/K (Air heat capacity)
    private static final double L = 2.5e6; // J/kg (Latent heat of vaporization)
    private static final double SIGMA = 5.67e-8;

    // Velocity fields for GCM (Winds)
    private double[][][] u; // Zonal wind [layer][lat][long]
    private double[][][] v; // Meridional wind
    private double[][][] w; // Vertical wind

    public enum PrecisionMode {
        REALS,
        PRIMITIVES
    }

    private PrecisionMode mode = PrecisionMode.PRIMITIVES;
    private org.jscience.mathematics.numbers.real.Real[][][] temperatureReal;
    private org.jscience.mathematics.numbers.real.Real[][] humidityReal;

    public GeneralCirculationModelTask(int latBins, int longBins) {
        this.latBins = latBins;
        this.longBins = longBins;
        this.temperature = new double[3][latBins][longBins];
        this.specificHumidity = new double[latBins][longBins];
        this.precipitation = new double[latBins][longBins];
        this.u = new double[3][latBins][longBins];
        this.v = new double[3][latBins][longBins];
        this.w = new double[3][latBins][longBins];
        initialize();
    }

    public GeneralCirculationModelTask() {
        this(0, 0);
    }

    private void initialize() {
        if (latBins == 0)
            return;
        for (int i = 0; i < latBins; i++) {
            double lat = Math.PI * (i - latBins / 2.0) / latBins;
            for (int j = 0; j < longBins; j++) {
                temperature[0][i][j] = 288.0 - 40 * Math.sin(lat) * Math.sin(lat); // Surface
                temperature[1][i][j] = temperature[0][i][j] - 30; // Troposphere
                temperature[2][i][j] = 210.0; // Stratosphere
                specificHumidity[i][j] = 0.01 * Math.exp(-Math.abs(lat));
                // Initialize winds to small random/zonal values
                u[0][i][j] = 10.0 * Math.random(); // Surface winds
                u[1][i][j] = 20.0 + 5.0 * Math.random(); // Jet streamish
                u[2][i][j] = 0.0;
            }
        }
    }

    @Override
    public Class<GeneralCirculationModelTask> getInputType() {
        return GeneralCirculationModelTask.class;
    }

    @Override
    public Class<GeneralCirculationModelTask> getOutputType() {
        return GeneralCirculationModelTask.class;
    }

    @Override
    public GeneralCirculationModelTask execute(GeneralCirculationModelTask input) {
        if (input != null && input.latBins > 0) {
            input.step(3600); // Run 1 hour step
            return input;
        }
        if (this.latBins > 0) {
            this.step(3600);
            return this;
        }
        return null;
    }

    @Override
    public String getTaskType() {
        return "GCM_CLIMATE";
    }

    public void setMode(PrecisionMode mode) {
        this.mode = mode;
        if (mode == PrecisionMode.REALS && temperatureReal == null) {
            syncToReal();
        }
    }

    private void syncToReal() {
        temperatureReal = new org.jscience.mathematics.numbers.real.Real[3][latBins][longBins];
        humidityReal = new org.jscience.mathematics.numbers.real.Real[latBins][longBins];
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    temperatureReal[k][i][j] = org.jscience.mathematics.numbers.real.Real.of(temperature[k][i][j]);
                }
            }
        }
        for (int i = 0; i < latBins; i++) {
            for (int j = 0; j < longBins; j++) {
                humidityReal[i][j] = org.jscience.mathematics.numbers.real.Real.of(specificHumidity[i][j]);
            }
        }
    }

    private void syncFromReal() {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    temperature[k][i][j] = temperatureReal[k][i][j].doubleValue();
                }
            }
        }
        for (int i = 0; i < latBins; i++) {
            for (int j = 0; j < longBins; j++) {
                specificHumidity[i][j] = humidityReal[i][j].doubleValue();
            }
        }
    }

    public void step(double dt) {
        if (mode == PrecisionMode.REALS) {
            // JScience Mode: Use Real-based Providers (LBM/NS)
            org.jscience.technical.backend.algorithms.NavierStokesProvider nsProvider = new org.jscience.technical.backend.algorithms.MulticoreNavierStokesProvider();

            // Flatten state for NavierStokesProvider (Real[])
            int size = 3 * latBins * longBins;
            org.jscience.mathematics.numbers.real.Real[] flatDensity = new org.jscience.mathematics.numbers.real.Real[size];
            org.jscience.mathematics.numbers.real.Real[] flatU = new org.jscience.mathematics.numbers.real.Real[size];
            org.jscience.mathematics.numbers.real.Real[] flatV = new org.jscience.mathematics.numbers.real.Real[size];
            org.jscience.mathematics.numbers.real.Real[] flatW = new org.jscience.mathematics.numbers.real.Real[size];

            int idx = 0;
            for (int k = 0; k < 3; k++) {
                for (int i = 0; i < latBins; i++) {
                    for (int j = 0; j < longBins; j++) {
                        flatDensity[idx] = temperatureReal[k][i][j];
                        flatU[idx] = org.jscience.mathematics.numbers.real.Real.of(u[k][i][j]);
                        flatV[idx] = org.jscience.mathematics.numbers.real.Real.of(v[k][i][j]);
                        flatW[idx] = org.jscience.mathematics.numbers.real.Real.of(w[k][i][j]);
                        idx++;
                    }
                }
            }

            nsProvider.solve(flatDensity, flatU, flatV, flatW,
                    org.jscience.mathematics.numbers.real.Real.of(dt),
                    org.jscience.mathematics.numbers.real.Real.of(0.0001),
                    longBins, latBins, 3);

            // Unpack
            idx = 0;
            for (int k = 0; k < 3; k++) {
                for (int i = 0; i < latBins; i++) {
                    for (int j = 0; j < longBins; j++) {
                        temperatureReal[k][i][j] = flatDensity[idx];
                        u[k][i][j] = flatU[idx].doubleValue();
                        v[k][i][j] = flatV[idx].doubleValue();
                        w[k][i][j] = flatW[idx].doubleValue();
                        idx++;
                    }
                }
            }
            // Add other radiative logic for Real mode if necessary, but focusing on
            // provider integration
            syncFromReal();
        } else {
            // Primitive Mode: Use side-by-side Support
            GCMPrimitiveSupport support = new GCMPrimitiveSupport();
            support.step(temperature, specificHumidity, u, v, w, dt, latBins, longBins);
        }
    }

    // Getters and helper methods (copied from original)
    public double getSurfaceTemperatureAt(int lat, int lon) {
        if (isValidCoordinate(lat, lon))
            return temperature[0][lat][lon];
        return 0.0;
    }

    public double[][] getSurfaceTemperature() {
        return temperature[0];
    }

    public double getAirTemperatureAt(int lat, int lon) {
        if (isValidCoordinate(lat, lon))
            return temperature[1][lat][lon];
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
