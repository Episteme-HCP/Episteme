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
 * Represents a laboratory centrifuge for sample separation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Centrifuge implements Device {

    public enum RotorType {
        FIXED_ANGLE, SWINGING_BUCKET, VERTICAL
    }

    private final String name;
    private final Real maxRPM;
    private final Real maxRCF;
    private final RotorType rotorType;
    private Real currentRPM;
    private boolean connected;
    private boolean running;

    public Centrifuge(String name, Real maxRPM, Real maxRCF, RotorType rotorType) {
        this.name = name;
        this.maxRPM = maxRPM;
        this.maxRCF = maxRCF;
        this.rotorType = rotorType;
        this.currentRPM = Real.ZERO;
    }

    @Override
    public void connect() throws IOException {
        this.connected = true;
    }

    @Override
    public void disconnect() throws IOException {
        stop();
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

    public void start(Real rpm) {
        if (rpm.compareTo(maxRPM) > 0) {
            throw new IllegalArgumentException("RPM exceeds maximum: " + maxRPM);
        }
        this.currentRPM = rpm;
        this.running = true;
    }

    public void stop() {
        this.currentRPM = Real.ZERO;
        this.running = false;
    }

    public Real calculateRCF(Real radiusCm) {
        Real factor = Real.of(1.118e-5);
        return factor.multiply(radiusCm).multiply(currentRPM.pow(2));
    }

    public Real getMaxRPM() {
        return maxRPM;
    }

    public Real getMaxRCF() {
        return maxRCF;
    }

    public RotorType getRotorType() {
        return rotorType;
    }

    public Real getCurrentRPM() {
        return currentRPM;
    }

    public boolean isRunning() {
        return running;
    }
}
