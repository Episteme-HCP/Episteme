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
 * Represents a microscope for magnified observation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Microscope implements Device {

    public enum Type {
        OPTICAL, ELECTRON_SCANNING, ELECTRON_TRANSMISSION, CONFOCAL, FLUORESCENCE, ATOMIC_FORCE
    }

    private final String name;
    private final Type type;
    private final Real maxMagnification;
    private final Real resolution;
    private Real currentMagnification;
    private boolean connected;

    public Microscope(String name, Type type, Real maxMagnification, Real resolution) {
        this.name = name;
        this.type = type;
        this.maxMagnification = maxMagnification;
        this.resolution = resolution;
        this.currentMagnification = Real.ONE;
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

    public void setMagnification(Real magnification) {
        if (magnification.compareTo(maxMagnification) > 0) {
            throw new IllegalArgumentException("Magnification exceeds maximum");
        }
        this.currentMagnification = magnification;
    }

    public Real getApparentSize(Real actualSize) {
        return actualSize.multiply(currentMagnification);
    }

    public boolean isResolvable(Real featureSize) {
        return featureSize.compareTo(resolution) >= 0;
    }

    public Type getType() {
        return type;
    }

    public Real getMaxMagnification() {
        return maxMagnification;
    }

    public Real getResolution() {
        return resolution;
    }

    public Real getCurrentMagnification() {
        return currentMagnification;
    }

    public static Microscope opticalMicroscope() {
        return new Microscope("Optical Microscope", Type.OPTICAL, Real.of(1000), Real.of(200));
    }
}
