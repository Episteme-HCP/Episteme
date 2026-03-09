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

import org.episteme.core.device.sim.AbstractSimulatedSensor;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.measure.quantity.Time;
import org.episteme.natural.device.sensors.Spectrometer;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;

/**
 * Simulated spectrometer.
 */
public class SimulatedSpectrometer extends AbstractSimulatedSensor<Dimensionless> implements Spectrometer {

    private final SpectroscopyType type;
    private final Quantity<Length> minWavelength;
    private final Quantity<Length> maxWavelength;
    private final Quantity<Length> spectralResolution;
    private Quantity<Time> integrationTime = Quantities.create(10.0, Units.SECOND);

    public SimulatedSpectrometer(Identification id, SpectroscopyType type, double minNm, double maxNm, double resNm) {
        super(id);
        this.type = type;
        this.minWavelength = Quantities.create(minNm, Units.NANOMETER);
        this.maxWavelength = Quantities.create(maxNm, Units.NANOMETER);
        this.spectralResolution = Quantities.create(resNm, Units.NANOMETER);
    }

    @Override
    public SpectroscopyType getType() {
        return type;
    }

    @Override
    public Quantity<Length> getMinWavelength() {
        return minWavelength;
    }

    @Override
    public Quantity<Length> getMaxWavelength() {
        return maxWavelength;
    }

    @Override
    public Quantity<Length> getSpectralResolution() {
        return spectralResolution;
    }

    @Override
    public void setIntegrationTime(Quantity<Time> time) {
        this.integrationTime = time;
    }

    @Override
    public Quantity<Time> getIntegrationTime() {
        return integrationTime;
    }

    @Override
    public double[][] captureSpectrum() {
        return new double[100][2];
    }

    @Override
    public Quantity<Dimensionless> getIntensityAt(Quantity<Length> wavelength) {
        return Quantities.create(0.5, Units.ONE);
    }

    @Override
    public void calibrate(Quantity<Length>[] ref, Quantity<Length>[] measured) {
        // No-op
    }

    @Override
    public Quantity<Dimensionless> readValue() throws IOException {
        return getIntensityAt(minWavelength);
    }
}
