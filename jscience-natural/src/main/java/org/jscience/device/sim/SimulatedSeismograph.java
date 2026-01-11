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

import org.jscience.device.sensors.Seismograph;
import org.jscience.mathematics.numbers.real.Real;
import java.util.Random;

/**
 * Simulated Seismograph device.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedSeismograph extends SimulatedDevice implements Seismograph {

    private final Random random = new Random();
    private double currentMagnitude = 0;

    public SimulatedSeismograph(String name) {
        super(name, "GeoSim Systems");
    }

    public SimulatedSeismograph() {
        this("Seismograph");
    }

    @Override
    public double readMagnitude() {
        if (!isConnected())
            return 0;
        // Simulate random tremors
        double noise = (random.nextGaussian() * 0.5);
        if (random.nextDouble() > 0.95) {
            currentMagnitude = 3.0 + random.nextDouble() * 4.0; // Earthquake spike
        }
        currentMagnitude *= 0.9; // Decay
        return Math.max(0, currentMagnitude + noise);
    }

    @Override
    public Real readValue() throws java.io.IOException {
        return Real.of(readMagnitude());
    }
}
