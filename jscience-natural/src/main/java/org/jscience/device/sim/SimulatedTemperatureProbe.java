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

package org.jscience.device.sim;

import org.jscience.device.sensors.TemperatureProbe;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of TemperatureProbe.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedTemperatureProbe extends SimulatedDevice implements TemperatureProbe {

    private final ProbeType type;
    private final Real accuracy;
    private final Real minTemp;
    private final Real maxTemp;
    private Real lastReading = Real.of(293.15); // 20 C

    public SimulatedTemperatureProbe(String name, ProbeType type, Real accuracy, Real minTemp, Real maxTemp) {
        super(name);
        this.type = type;
        this.accuracy = accuracy;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    @Override
    public ProbeType getType() {
        return type;
    }

    @Override
    public Real getAccuracy() {
        return accuracy;
    }

    @Override
    public Real getMinTemp() {
        return minTemp;
    }

    @Override
    public Real getMaxTemp() {
        return maxTemp;
    }

    private final double thermalInertia = 0.2; // 0.2 means it takes a few steps to reach target

    @Override
    public Real measure(Real actualTemp) {
        if (!isConnected())
            throw new IllegalStateException("Device not connected");

        // Thermal inertia simulation: lastReading moves toward actualTemp
        double current = lastReading.doubleValue();
        double target = actualTemp.doubleValue();
        double next = current + (target - current) * thermalInertia;

        // Add measurement noise
        double noise = (Math.random() - 0.5) * accuracy.doubleValue();
        lastReading = Real.of(next + noise);

        return lastReading;
    }

    @Override
    public Real readValue() throws IOException {
        if (!isConnected())
            throw new IOException("Device not connected");
        return lastReading;
    }
}
