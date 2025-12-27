/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.devices.sim;

import org.jscience.devices.sensors.PressureGauge;
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

    private final GaugeType type;
    private final Real accuracy;
    private Real basePressure = Real.of(101.325); // 1 atm in kPa
    private Real minPressure = Real.of(0.0);
    private Real maxPressure = Real.of(1000.0); // 1000 kPa = 10 bar
    private Real lastReading = Real.of(101.325);
    private String pressureUnit = "kPa";

    public SimulatedPressureGauge(String name) {
        super(name);
        this.type = GaugeType.PIEZOELECTRIC;
        this.accuracy = Real.of(0.5);
    }

    public SimulatedPressureGauge(String name, GaugeType type, Real accuracy, Real minPressure, Real maxPressure) {
        super(name);
        this.type = type;
        this.accuracy = accuracy;
        this.minPressure = minPressure;
        this.maxPressure = maxPressure;
    }

    @Override
    public GaugeType getType() {
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
