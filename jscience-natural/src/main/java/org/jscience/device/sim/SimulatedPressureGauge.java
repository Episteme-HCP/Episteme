/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.device.sim;

import org.jscience.device.sensors.PressureGauge;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated pressure gauge for testing pressure measurement workflows.
 * Generates realistic pressure readings with optional drift and noise.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedPressureGauge extends SimulatedDevice implements PressureGauge {

    private final PressureGauge.GaugeType type;
    private final Real accuracy;
    private Real basePressure = Real.of(101.325); // 1 atm in kPa
    private Real minPressure = Real.of(0.0);
    private Real maxPressure = Real.of(1000.0); // 1000 kPa = 10 bar
    private Real lastReading = Real.of(101.325);
    private String pressureUnit = "kPa";

    public SimulatedPressureGauge() {
        this("Simulated PressureGauge");
    }

    public SimulatedPressureGauge(String name) {
        super(name);
        this.type = PressureGauge.GaugeType.PIEZOELECTRIC;
        this.accuracy = Real.of(0.5);
    }

    public SimulatedPressureGauge(String name, PressureGauge.GaugeType type, Real accuracy, Real minPressure,
            Real maxPressure) {
        super(name);
        this.type = type;
        this.accuracy = accuracy;
        this.minPressure = minPressure;
        this.maxPressure = maxPressure;
    }

    @Override
    public PressureGauge.GaugeType getType() {
        return type;
    }

    @Override
    public Real getAccuracy() {
        return accuracy;
    }

    @Override
    public Real getMinPressure() {
        return minPressure;
    }

    @Override
    public Real getMaxPressure() {
        return maxPressure;
    }

    @Override
    public String getPressureUnit() {
        return pressureUnit;
    }

    @Override
    public Real measure(Real actualPressure) {
        if (!isConnected()) {
            throw new IllegalStateException("Device not connected");
        }
        // Simple noise simulation
        double noise = (Math.random() - 0.5) * accuracy.doubleValue();
        lastReading = actualPressure.add(Real.of(noise));
        return lastReading;
    }

    @Override
    public Real readValue() throws IOException {
        if (!isConnected()) {
            throw new IOException("Device not connected");
        }

        // Simulating environmental fluctuations (atmospheric tides, wind etc)
        // A slowly varying sine component + measurement noise
        double time = System.currentTimeMillis() / 10000.0; // Very slow
        double fluctuation = 0.2 * Math.sin(time); // +/- 0.2 kPa

        double noise = (Math.random() - 0.5) * accuracy.doubleValue();
        lastReading = basePressure.add(Real.of(fluctuation + noise));
        return lastReading;
    }

    /**
     * Sets the base pressure for simulation.
     */
    public void setBasePressure(Real pressure) {
        this.basePressure = pressure;
    }

    /**
     * Gets the current base pressure.
     */
    public Real getBasePressure() {
        return basePressure;
    }

    /**
     * Sets the pressure unit.
     */
    public void setPressureUnit(String unit) {
        this.pressureUnit = unit;
    }
}
