/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.episteme.server.server.tasks.earth.climate;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskRegistry;

/**
 * Advanced General Circulation Model (GCM) Task.
 
 * <p>
 * <b>Reference:</b><br>
 * Zeigler, B. P., Praehofer, H., & Kim, T. G. (2000). <i>Theory of Modeling and Simulation</i>. Academic Press.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeneralCirculationModelTask
        implements DistributedTask<GeneralCirculationModelTask, GeneralCirculationModelTask> {

    private final int latBins;
    private final int longBins;

    // Layers: 0 = Surface, 1 = Troposphere, 2 = Stratosphere
    private double[][][] temperature; // [layer][lat][long]
    private double[][] specificHumidity; // [lat][long]

    // Velocity fields for GCM (Winds)
    private double[][][] u; // Zonal wind [layer][lat][long]
    private double[][][] v; // Meridional wind
    private double[][][] w; // Vertical wind



    private TaskRegistry.PrecisionMode mode = TaskRegistry.PrecisionMode.DOUBLE;
    private float[][][] temperatureFloat;
    private float[][] humidityFloat;
    private float[][][] uFloat;
    private float[][][] vFloat;
    private float[][][] wFloat;

    private org.episteme.core.mathematics.numbers.real.Real[][][] temperatureReal;
    private org.episteme.core.mathematics.numbers.real.Real[][] humidityReal;

    public GeneralCirculationModelTask(int latBins, int longBins) {
        this.latBins = latBins;
        this.longBins = longBins;
        this.temperature = new double[3][latBins][longBins];
        this.specificHumidity = new double[latBins][longBins];
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

    public void setMode(TaskRegistry.PrecisionMode mode) {
        this.mode = mode;
        if (mode == TaskRegistry.PrecisionMode.REAL && temperatureReal == null) {
            syncToReal();
        } else if (mode == TaskRegistry.PrecisionMode.FLOAT && temperatureFloat == null) {
            syncToFloat();
        }
    }

    private void syncToFloat() {
        temperatureFloat = new float[3][latBins][longBins];
        humidityFloat = new float[latBins][longBins];
        uFloat = new float[3][latBins][longBins];
        vFloat = new float[3][latBins][longBins];
        wFloat = new float[3][latBins][longBins];
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    temperatureFloat[k][i][j] = (float) temperature[k][i][j];
                    uFloat[k][i][j] = (float) u[k][i][j];
                    vFloat[k][i][j] = (float) v[k][i][j];
                    wFloat[k][i][j] = (float) w[k][i][j];
                }
            }
        }
        for (int i = 0; i < latBins; i++) {
            for (int j = 0; j < longBins; j++) {
                humidityFloat[i][j] = (float) specificHumidity[i][j];
            }
        }
    }

    private void syncFromFloat() {
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    temperature[k][i][j] = (double) temperatureFloat[k][i][j];
                    u[k][i][j] = (double) uFloat[k][i][j];
                    v[k][i][j] = (double) vFloat[k][i][j];
                    w[k][i][j] = (double) wFloat[k][i][j];
                }
            }
        }
        for (int i = 0; i < latBins; i++) {
            for (int j = 0; j < longBins; j++) {
                specificHumidity[i][j] = (double) humidityFloat[i][j];
            }
        }
    }

    private void syncToReal() {
        temperatureReal = new org.episteme.core.mathematics.numbers.real.Real[3][latBins][longBins];
        humidityReal = new org.episteme.core.mathematics.numbers.real.Real[latBins][longBins];
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    temperatureReal[k][i][j] = org.episteme.core.mathematics.numbers.real.Real.of(temperature[k][i][j]);
                }
            }
        }
        for (int i = 0; i < latBins; i++) {
            for (int j = 0; j < longBins; j++) {
                humidityReal[i][j] = org.episteme.core.mathematics.numbers.real.Real.of(specificHumidity[i][j]);
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
        org.episteme.natural.physics.classical.matter.fluids.NavierStokesProvider nsProvider = new org.episteme.natural.physics.classical.matter.fluids.providers.MulticoreNavierStokesProvider();

        switch (mode) {
            case REAL -> runRealStep(nsProvider, dt);
            case FLOAT -> runFloatStep(nsProvider, dt);
            default -> runPrimitiveStep(nsProvider, dt);
        }
    }

    private void runFloatStep(org.episteme.natural.physics.classical.matter.fluids.NavierStokesProvider provider, double dt) {
        int size = 3 * latBins * longBins;
        float[] flatDensity = new float[size];
        float[] flatU = new float[size];
        float[] flatV = new float[size];
        float[] flatW = new float[size];

        int idx = 0;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    flatDensity[idx] = temperatureFloat[k][i][j];
                    flatU[idx] = uFloat[k][i][j];
                    flatV[idx] = vFloat[k][i][j];
                    flatW[idx] = wFloat[k][i][j];
                    idx++;
                }
            }
        }

        provider.solve(flatDensity, flatU, flatV, flatW, (float) dt, 0.0001f, longBins, latBins, 3);

        idx = 0;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    temperatureFloat[k][i][j] = flatDensity[idx];
                    uFloat[k][i][j] = flatU[idx];
                    vFloat[k][i][j] = flatV[idx];
                    wFloat[k][i][j] = flatW[idx];
                    idx++;
                }
            }
        }
        syncFromFloat();
    }

    private void runRealStep(org.episteme.natural.physics.classical.matter.fluids.NavierStokesProvider provider, double dt) {
        int size = 3 * latBins * longBins;
        org.episteme.core.mathematics.numbers.real.Real[] flatDensity = new org.episteme.core.mathematics.numbers.real.Real[size];
        org.episteme.core.mathematics.numbers.real.Real[] flatU = new org.episteme.core.mathematics.numbers.real.Real[size];
        org.episteme.core.mathematics.numbers.real.Real[] flatV = new org.episteme.core.mathematics.numbers.real.Real[size];
        org.episteme.core.mathematics.numbers.real.Real[] flatW = new org.episteme.core.mathematics.numbers.real.Real[size];

        int idx = 0;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    flatDensity[idx] = temperatureReal[k][i][j];
                    flatU[idx] = org.episteme.core.mathematics.numbers.real.Real.of(u[k][i][j]);
                    flatV[idx] = org.episteme.core.mathematics.numbers.real.Real.of(v[k][i][j]);
                    flatW[idx] = org.episteme.core.mathematics.numbers.real.Real.of(w[k][i][j]);
                    idx++;
                }
            }
        }

        provider.solve(flatDensity, flatU, flatV, flatW,
                org.episteme.core.mathematics.numbers.real.Real.of(dt),
                org.episteme.core.mathematics.numbers.real.Real.of(0.0001),
                longBins, latBins, 3);

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
        syncFromReal();
    }

    private void runPrimitiveStep(org.episteme.natural.physics.classical.matter.fluids.NavierStokesProvider provider, double dt) {
        int size = 3 * latBins * longBins;
        double[] flatDensity = new double[size];
        double[] flatU = new double[size];
        double[] flatV = new double[size];
        double[] flatW = new double[size];

        int idx = 0;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    flatDensity[idx] = temperature[k][i][j];
                    flatU[idx] = u[k][i][j];
                    flatV[idx] = v[k][i][j];
                    flatW[idx] = w[k][i][j];
                    idx++;
                }
            }
        }

        provider.solve(flatDensity, flatU, flatV, flatW, dt, 0.0001, longBins, latBins, 3);

        idx = 0;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < latBins; i++) {
                for (int j = 0; j < longBins; j++) {
                    temperature[k][i][j] = flatDensity[idx];
                    u[k][i][j] = flatU[idx];
                    v[k][i][j] = flatV[idx];
                    w[k][i][j] = flatW[idx];
                    idx++;
                }
            }
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
