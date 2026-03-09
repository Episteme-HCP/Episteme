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

import org.episteme.core.device.sim.SimulatedDevice;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.core.measure.quantity.Length;
import org.episteme.natural.device.sensors.Microscope;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;

/**
 * Simulated implementation of Microscope.
 */
public class SimulatedMicroscope extends SimulatedDevice implements Microscope {

    private final Type type;
    private final Quantity<Dimensionless> maxMagnification;
    private final Quantity<Length> opticalResolution;
    private Quantity<Dimensionless> currentMagnification = Quantities.create(1.0, Units.ONE);

    public SimulatedMicroscope(Identification id, Type type, double maxMag, double resNm) {
        super(id);
        this.type = type;
        this.maxMagnification = Quantities.create(maxMag, Units.ONE);
        this.opticalResolution = Quantities.create(resNm, Units.NANOMETER);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Quantity<Dimensionless> getMaxMagnification() {
        return maxMagnification;
    }

    @Override
    public Quantity<Length> getOpticalResolution() {
        return opticalResolution;
    }

    @Override
    public Quantity<Dimensionless> getCurrentMagnification() {
        return currentMagnification;
    }

    @Override
    public void setMagnification(Quantity<Dimensionless> magnification) {
        this.currentMagnification = magnification;
    }

    @Override
    public Quantity<Length> getApparentSize(Quantity<Length> actualSize) {
        double mag = currentMagnification.getValue().doubleValue();
        return actualSize.multiply(mag);
    }

    @Override
    public boolean isResolvable(Quantity<Length> featureSize) {
        return featureSize.compareTo(opticalResolution) >= 0;
    }

    @Override
    public Quantity<Dimensionless> readValue() throws IOException {
        return currentMagnification;
    }
}
