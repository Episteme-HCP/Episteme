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

import org.jscience.device.actuators.Centrifuge;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulated implementation of Centrifuge.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedCentrifuge extends SimulatedDevice implements Centrifuge {

    private final Real maxRPM;
    private final Real maxRCF;
    private final Centrifuge.RotorType rotorType;
    private Real currentRPM = Real.ZERO;
    private boolean running = false;

    public SimulatedCentrifuge() {
        this("Centrifuge", Real.of(15000), Real.of(25000), Centrifuge.RotorType.FIXED_ANGLE);
    }

    public SimulatedCentrifuge(String name, Real maxRPM, Real maxRCF, Centrifuge.RotorType rotorType) {
        super(name);
        this.maxRPM = maxRPM;
        this.maxRCF = maxRCF;
        this.rotorType = rotorType;
    }

    @Override
    public void stop() {
        this.currentRPM = Real.ZERO;
        this.running = false;
    }

    @Override
    public Real calculateRCF(Real radiusCm) {
        // RCF = 1.118e-5 * r * rpm^2
        double r = radiusCm.doubleValue();
        double rpm = currentRPM.doubleValue();
        return Real.of(1.118e-5 * r * rpm * rpm);
    }

    @Override
    public Real getMaxRPM() {
        return maxRPM;
    }

    @Override
    public Real getMaxRCF() {
        return maxRCF;
    }

    @Override
    public RotorType getRotorType() {
        return rotorType;
    }

    @Override
    public Real getCurrentRPM() {
        if (running && currentRPM.doubleValue() < targetRPM.doubleValue()) {
            // Mock acceleration: 50 RPM per call for simulation speed
            double next = Math.min(targetRPM.doubleValue(), currentRPM.doubleValue() + 50);
            currentRPM = Real.of(next);
        } else if (!running && currentRPM.doubleValue() > 0) {
            // Mock deceleration
            double next = Math.max(0, currentRPM.doubleValue() - 50);
            currentRPM = Real.of(next);
        }
        return currentRPM;
    }

    private Real targetRPM = Real.ZERO;

    @Override
    public void start(Real rpm) {
        if (rpm.compareTo(maxRPM) > 0) {
            throw new IllegalArgumentException("RPM exceeds maximum: " + maxRPM);
        }
        this.targetRPM = rpm;
        this.running = true;
    }

    @Override
    public boolean isRunning() {
        return running || currentRPM.doubleValue() > 0;
    }

    @Override
    public void send(Real command) throws java.io.IOException {
        start(command);
    }
}


