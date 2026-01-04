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

import org.jscience.device.sensors.Microscope;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Simulated implementation of Microscope.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedMicroscope extends SimulatedDevice implements Microscope {

    private final Type type;
    private final Real maxMagnification;
    private final Real resolution;
    private Real currentMagnification = Real.ONE;

    public SimulatedMicroscope() {
        this("Microscope", Type.OPTICAL, Real.of(1000), Real.of(0.0002));
    }

    public SimulatedMicroscope(String name, Type type, Real maxMagnification, Real resolution) {
        super(name);
        this.type = type;
        this.maxMagnification = maxMagnification;
        this.resolution = resolution;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Real getMaxMagnification() {
        return maxMagnification;
    }

    @Override
    public Real getResolution() {
        return resolution;
    }

    @Override
    public Real getCurrentMagnification() {
        return currentMagnification;
    }

    @Override
    public void setMagnification(Real magnification) {
        if (magnification.compareTo(maxMagnification) > 0) {
            throw new IllegalArgumentException("Magnification exceeds maximum");
        }
        this.currentMagnification = magnification;
    }

    private double focus = 0.5;
    private double lightingLevel = 0.8;

    @Override
    public Real getApparentSize(Real actualSize) {
        return actualSize.multiply(currentMagnification);
    }

    @Override
    public boolean isResolvable(Real featureSize) {
        // Feature is resolvable if it's larger than resolution,
        // but also depends on focus and lighting.
        double effectiveResolution = resolution.doubleValue();

        // Poor focus increases the minimum resolvable size
        effectiveResolution /= (0.1 + 0.9 * focus);

        // Poor lighting decreases contrast and prevents resolution
        if (lightingLevel < 0.2)
            return false;

        return featureSize.doubleValue() >= effectiveResolution;
    }

    public void setFocus(double focus) {
        this.focus = Math.max(0, Math.min(1.0, focus));
    }

    public void setLightingLevel(double level) {
        this.lightingLevel = Math.max(0, Math.min(1.0, level));
    }

    @Override
    public Real readValue() throws java.io.IOException {
        // Return current magnification as the "value" for now, or could be image data
        // in future
        return currentMagnification;
    }
}
