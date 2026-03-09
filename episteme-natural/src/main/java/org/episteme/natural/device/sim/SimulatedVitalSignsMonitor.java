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

package org.episteme.natural.device.sim;

import org.episteme.core.device.sim.AbstractSimulatedDevice;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Frequency;
import org.episteme.natural.medicine.VitalSigns;
import org.episteme.natural.medicine.VitalSignsMonitor;

import java.util.Random;

/**
 * Simulated vital signs monitor producing realistic medical data.
 */
public class SimulatedVitalSignsMonitor extends AbstractSimulatedDevice implements VitalSignsMonitor {

    private final Random random = new Random();
    private final double sampleRate = 250.0;
    private final int waveformSize = 1000;

    private int baseHeartRate = 89;
    private int baseSystolic = 108;
    private int baseDiastolic = 68;
    private int baseSpO2 = 99;
    private int baseRespRate = 16;
    private double baseTemp = 98.6;

    private double time = 0;
    private double[] ecgBuffer = new double[waveformSize];
    private double[] plethBuffer = new double[waveformSize];
    private int bufferIndex = 0;

    public SimulatedVitalSignsMonitor(Identification id) {
        super(id);
        for (int i = 0; i < waveformSize; i++) {
            ecgBuffer[i] = 0;
            plethBuffer[i] = 0;
        }
    }

    public int getBaseHeartRate() { return baseHeartRate; }
    public void setBaseHeartRate(int baseHeartRate) { this.baseHeartRate = baseHeartRate; }
    public int getBaseSpO2() { return baseSpO2; }
    public void setBaseSpO2(int baseSpO2) { this.baseSpO2 = baseSpO2; }

    @Override
    public VitalSigns getVitalSigns() {
        int hr = baseHeartRate + random.nextInt(3) - 1;
        int sys = baseSystolic + random.nextInt(5) - 2;
        int dia = baseDiastolic + random.nextInt(3) - 1;

        int spo2Noise = (baseSpO2 >= 99) ? 0 : random.nextInt(2) - 1;
        int spo2 = Math.min(100, Math.max(0, baseSpO2 + spo2Noise));

        int rr = baseRespRate + random.nextInt(2) - 1;
        double temp = baseTemp + (random.nextDouble() - 0.5) * 0.1;

        return new VitalSigns(hr, sys, dia, spo2, rr, temp);
    }

    @Override
    public double[] getECGWaveform() {
        updateWaveforms();
        return getOrderedBuffer(ecgBuffer);
    }

    @Override
    public double[] getPlethWaveform() {
        return getOrderedBuffer(plethBuffer);
    }

    private double[] getOrderedBuffer(double[] buffer) {
        double[] ordered = new double[waveformSize];
        for (int i = 0; i < waveformSize; i++) {
            ordered[i] = buffer[(bufferIndex + i) % waveformSize];
        }
        return ordered;
    }

    @Override
    public int getChannelCount() {
        return 2;
    }

    @Override
    public boolean isAlarming() {
        return false;
    }

    @Override
    public double getSampleRate() {
        return sampleRate;
    }

    @Override
    public java.util.Optional<Quantity<?>> getValue() {
        // Return Heart Rate as the primary numeric value
        return java.util.Optional.of(Quantities.create(getVitalSigns().heartRate().getValue(), Units.HERTZ.divide(60).asType(Frequency.class)));
    }

    private void updateWaveforms() {
        double dt = 1.0 / sampleRate;
        double heartPeriod = 60.0 / baseHeartRate;

        for (int i = 0; i < 5; i++) {
            time += dt;
            double cardiacPhase = (time % heartPeriod) / heartPeriod;
            ecgBuffer[bufferIndex] = generateECG(cardiacPhase);
            plethBuffer[bufferIndex] = generatePleth(cardiacPhase);
            bufferIndex = (bufferIndex + 1) % waveformSize;
        }
    }

    private double generateECG(double phase) {
        double val = 0;
        if (phase < 0.1) val = 0.15 * Math.sin(phase / 0.1 * Math.PI);
        else if (phase < 0.16) val = 0;
        else if (phase < 0.18) val = -0.1 * Math.sin((phase - 0.16) / 0.02 * Math.PI);
        else if (phase < 0.22) val = Math.sin((phase - 0.18) / 0.04 * Math.PI);
        else if (phase < 0.26) val = -0.2 * Math.sin((phase - 0.22) / 0.04 * Math.PI);
        else if (phase < 0.4) val = 0.02;
        else if (phase < 0.55) val = 0.3 * Math.sin((phase - 0.4) / 0.15 * Math.PI);
        val += (random.nextDouble() - 0.5) * 0.015;
        return val;
    }

    private double generatePleth(double phase) {
        double val = (phase < 0.15) ? Math.sin(phase / 0.15 * Math.PI / 2) :
                     (phase < 0.35) ? (1.0 - ((phase - 0.15) / 0.2) * 0.4) :
                     0.6 * Math.exp(-(phase - 0.35) / 0.65 * 3);
        if (phase > 0.2 && phase < 0.25) val -= 0.1 * Math.sin((phase - 0.2) / 0.05 * Math.PI);
        return val * 0.8 + 0.1 + (random.nextDouble() - 0.5) * 0.01;
    }
}
