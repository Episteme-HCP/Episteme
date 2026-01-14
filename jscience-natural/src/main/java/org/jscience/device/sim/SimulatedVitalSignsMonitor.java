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

import org.jscience.medicine.VitalSigns;
import org.jscience.medicine.VitalSignsMonitor;

import java.io.IOException;

import java.util.Random;

/**
 * Simulated vital signs monitor producing realistic medical data.
 * <p>
 * Generates synthetic ECG waveforms, plethysmograph (SpO2) signals,
 * and vital sign values with configurable noise and variation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedVitalSignsMonitor extends SimulatedDevice implements VitalSignsMonitor {

    private final Random random = new Random();
    private final double sampleRate = 250.0; // Hz
    private final int waveformSize = 1000; // Larger buffer for smoother display

    // Base vital values
    private int baseHeartRate = 89;
    private int baseSystolic = 108;
    private int baseDiastolic = 68;
    private int baseSpO2 = 99;
    private int baseRespRate = 16;
    private double baseTemp = 98.6;

    // Simulation state
    private double time = 0;
    private double[] ecgBuffer = new double[waveformSize];
    private double[] plethBuffer = new double[waveformSize];
    private int bufferIndex = 0;

    public SimulatedVitalSignsMonitor() {
        this("Vital Signs Monitor");
    }

    public SimulatedVitalSignsMonitor(String name) {
        super(name);
        // Initialize buffers
        for (int i = 0; i < waveformSize; i++) {
            ecgBuffer[i] = 0;
            plethBuffer[i] = 0;
        }
    }

    @Override
    public VitalSigns readValue() throws IOException {
        return getVitalSigns();
    }

    @Override
    public VitalSigns getVitalSigns() {
        // Add realistic variation
        int hr = baseHeartRate + random.nextInt(3) - 1;
        int sys = baseSystolic + random.nextInt(5) - 2;
        int dia = baseDiastolic + random.nextInt(3) - 1;

        // Fix SpO2 fluctuation: if high, keep stable.
        int spo2Noise = random.nextInt(2) - 1;
        if (baseSpO2 >= 99)
            spo2Noise = 0; // Stable if perfect saturation
        int spo2 = Math.min(100, Math.max(0, baseSpO2 + spo2Noise));

        int rr = baseRespRate + random.nextInt(2) - 1;
        double temp = baseTemp + (random.nextDouble() - 0.5) * 0.1; // Reduced temp noise

        return new VitalSigns(hr, sys, dia, spo2, rr, temp);
    }

    @Override
    public double[] getECGWaveform() {
        // Generate new samples
        updateWaveforms();
        // Return buffer in time order (oldest to newest)
        return getOrderedBuffer(ecgBuffer);
    }

    @Override
    public double[] getPlethWaveform() {
        // Return buffer in time order (oldest to newest)
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
        return 2; // ECG Lead II and SpO2
    }

    @Override
    public double getSampleRate() {
        return sampleRate;
    }

    /**
     * Sets the base heart rate for simulation.
     */
    public void setBaseHeartRate(int bpm) {
        this.baseHeartRate = bpm;
    }

    /**
     * Sets the base blood pressure for simulation.
     */
    public void setBaseBloodPressure(int systolic, int diastolic) {
        this.baseSystolic = systolic;
        this.baseDiastolic = diastolic;
    }

    /**
     * Sets the base SpO2 for simulation.
     */
    public void setBaseSpO2(int percent) {
        this.baseSpO2 = Math.min(100, Math.max(0, percent));
    }

    public int getBaseHeartRate() {
        return baseHeartRate;
    }

    public int getBaseSpO2() {
        return baseSpO2;
    }

    private void updateWaveforms() {
        double dt = 1.0 / sampleRate;
        double heartPeriod = 60.0 / baseHeartRate;

        for (int i = 0; i < 5; i++) { // Generate 5 new samples per call for smoother scrolling
            time += dt;

            // ECG waveform generation (Lead II pattern)
            double cardiacPhase = (time % heartPeriod) / heartPeriod;
            double ecgVal = generateECG(cardiacPhase);

            // Plethysmograph waveform (SpO2 pulse)
            // User requested curves corresponding to signals.
            // Currently we only output ECG and Pleth.
            // SpO2 curve (Red/IR absorption) IS the Plethysmograph.
            double plethVal = generatePleth(cardiacPhase);

            // Respiration modulation (Respiratory Sinus Arrhythmia simulation on Pleth
            // baseline)
            double respPhase = (time % (60.0 / baseRespRate)) / (60.0 / baseRespRate);
            double respMod = 0.05 * Math.sin(respPhase * 2 * Math.PI);
            plethVal += respMod;

            // Store in circular buffer
            ecgBuffer[bufferIndex] = ecgVal;
            plethBuffer[bufferIndex] = plethVal;
            bufferIndex = (bufferIndex + 1) % waveformSize;
        }
    }

    private double generateECG(double phase) {
        double val = 0;

        // P wave (0.0 - 0.1)
        if (phase >= 0.0 && phase < 0.1) {
            val = 0.15 * Math.sin(phase / 0.1 * Math.PI);
        }
        // PR segment (0.1 - 0.16)
        else if (phase >= 0.1 && phase < 0.16) {
            val = 0;
        }
        // Q wave (0.16 - 0.18)
        else if (phase >= 0.16 && phase < 0.18) {
            val = -0.1 * Math.sin((phase - 0.16) / 0.02 * Math.PI);
        }
        // R wave (0.18 - 0.22)
        else if (phase >= 0.18 && phase < 0.22) {
            double rPhase = (phase - 0.18) / 0.04;
            val = 1.0 * Math.sin(rPhase * Math.PI);
        }
        // S wave (0.22 - 0.26)
        else if (phase >= 0.22 && phase < 0.26) {
            val = -0.2 * Math.sin((phase - 0.22) / 0.04 * Math.PI);
        }
        // ST segment (0.26 - 0.4)
        else if (phase >= 0.26 && phase < 0.4) {
            val = 0.02;
        }
        // T wave (0.4 - 0.55)
        else if (phase >= 0.4 && phase < 0.55) {
            val = 0.3 * Math.sin((phase - 0.4) / 0.15 * Math.PI);
        }
        // Baseline
        else {
            val = 0;
        }

        // Add subtle noise
        val += (random.nextDouble() - 0.5) * 0.015;
        return val;
    }

    private double generatePleth(double phase) {
        // Plethysmograph has a characteristic pulse shape
        double val;

        if (phase < 0.15) {
            // Systolic upstroke
            val = Math.sin(phase / 0.15 * Math.PI / 2);
        } else if (phase < 0.35) {
            // Dicrotic notch and decay
            double decay = (phase - 0.15) / 0.2;
            val = 1.0 - decay * 0.4;
            // Add dicrotic notch
            if (phase > 0.2 && phase < 0.25) {
                val -= 0.1 * Math.sin((phase - 0.2) / 0.05 * Math.PI);
            }
        } else {
            // Diastolic decay
            double decay = (phase - 0.35) / 0.65;
            val = 0.6 * Math.exp(-decay * 3);
        }

        // Normalize to 0-1 range
        val = val * 0.8 + 0.1;

        // Add subtle noise
        val += (random.nextDouble() - 0.5) * 0.01;
        return val;
    }
}


