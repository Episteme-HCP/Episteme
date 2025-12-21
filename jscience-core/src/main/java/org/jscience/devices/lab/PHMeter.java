/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.devices.lab;

import org.jscience.devices.Device;
import org.jscience.mathematics.numbers.real.Real;

import java.io.IOException;

/**
 * Represents a pH meter for measuring acidity/alkalinity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PHMeter implements Device {

    public static final Real NEUTRAL_PH = Real.of(7.0);
    public static final Real MIN_PH = Real.ZERO;
    public static final Real MAX_PH = Real.of(14.0);

    private final String name;
    private final Real accuracy;
    private Real lastReading;
    private boolean connected;

    public PHMeter(String name, Real accuracy) {
        this.name = name;
        this.accuracy = accuracy;
        this.lastReading = NEUTRAL_PH;
    }

    @Override
    public void connect() throws IOException {
        this.connected = true;
    }

    @Override
    public void disconnect() throws IOException {
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }

    public Real measure(Real actualPH) {
        if (actualPH.compareTo(MIN_PH) < 0 || actualPH.compareTo(MAX_PH) > 0) {
            throw new IllegalArgumentException("pH must be between 0 and 14");
        }
        Real variation = accuracy.multiply(Real.of(Math.random() * 2 - 1));
        lastReading = actualPH.add(variation);
        return lastReading;
    }

    public Real read() {
        return lastReading;
    }

    public String classify(Real pH) {
        if (pH.compareTo(Real.of(7.0)) < 0) {
            if (pH.compareTo(Real.of(3.0)) < 0)
                return "Strongly Acidic";
            if (pH.compareTo(Real.of(5.0)) < 0)
                return "Moderately Acidic";
            return "Weakly Acidic";
        } else if (pH.compareTo(Real.of(7.0)) > 0) {
            if (pH.compareTo(Real.of(11.0)) > 0)
                return "Strongly Alkaline";
            if (pH.compareTo(Real.of(9.0)) > 0)
                return "Moderately Alkaline";
            return "Weakly Alkaline";
        }
        return "Neutral";
    }

    public Real getHydrogenConcentration(Real pH) {
        return Real.of(10).pow(pH.negate());
    }

    public Real getAccuracy() {
        return accuracy;
    }
}
