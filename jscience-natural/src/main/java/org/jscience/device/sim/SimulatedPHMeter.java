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

import org.jscience.device.sensors.PHMeter;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of PHMeter.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedPHMeter extends SimulatedDevice implements PHMeter {

    private final Real accuracy;
    private Real lastReading = PHMeter.NEUTRAL_PH;

    public SimulatedPHMeter() {
        this("pH Meter", Real.of(0.01));
    }

    public SimulatedPHMeter(String name, Real accuracy) {
        super(name);
        this.accuracy = accuracy;
    }

    @Override
    public Real getAccuracy() {
        return accuracy;
    }

    private Real calibrationOffset = Real.ZERO;

    @Override
    public Real measure(Real actualPH) {
        if (!isConnected())
            throw new IllegalStateException("Device not connected");

        if (actualPH.compareTo(PHMeter.MIN_PH) < 0 || actualPH.compareTo(PHMeter.MAX_PH) > 0) {
            throw new IllegalArgumentException("pH must be between 0 and 14");
        }

        // Add some noise and apply calibration offset
        double noise = (Math.random() - 0.5) * accuracy.doubleValue();
        lastReading = actualPH.add(Real.of(noise)).add(calibrationOffset);
        return lastReading;
    }

    @Override
    public Real readValue() throws IOException {
        if (!isConnected())
            throw new IOException("Device not connected");
        // Add a tiny bit of electronic noise even on stable reading
        double drift = (Math.random() - 0.5) * 0.005;
        return lastReading.add(Real.of(drift));
    }

    /**
     * Calibrates the meter using a known buffer.
     * 
     * @param bufferPH the known pH of the calibration buffer
     */
    public void calibrate(Real bufferPH) {
        // Simple 1-point calibration: adjust offset to match buffer
        double diff = bufferPH.doubleValue() - lastReading.doubleValue();
        this.calibrationOffset = Real.of(diff);
    }

    @Override
    public String classify(Real pH) {
        double val = pH.doubleValue();
        if (val < 7.0) {
            if (val < 3.0)
                return "Strongly Acidic";
            if (val < 5.0)
                return "Moderately Acidic";
            return "Weakly Acidic";
        } else if (val > 7.0) {
            if (val > 11.0)
                return "Strongly Alkaline";
            if (val > 9.0)
                return "Moderately Alkaline";
            return "Weakly Alkaline";
        }
        return "Neutral";
    }

    @Override
    public Real getHydrogenConcentration(Real pH) {
        return Real.of(Math.pow(10, -pH.doubleValue()));
    }
}


