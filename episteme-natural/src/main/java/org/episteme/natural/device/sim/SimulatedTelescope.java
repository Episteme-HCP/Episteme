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

import org.episteme.core.device.sim.AbstractSimulatedDevice;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Angle;
import org.episteme.natural.device.transducers.Telescope;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;
import java.util.Map;

/**
 * Simulated telescope.
 */
public class SimulatedTelescope extends AbstractSimulatedDevice implements Telescope {

    protected Quantity<Angle> ra;
    protected Quantity<Angle> dec;

    public SimulatedTelescope(Identification id) {
        super(id);
        this.ra = Quantities.create(0.0, Units.DEGREE_ANGLE);
        this.dec = Quantities.create(0.0, Units.DEGREE_ANGLE);
    }

    @Override
    public void slewTo(Quantity<Angle> ra, Quantity<Angle> dec) {
        this.ra = ra;
        this.dec = dec;
    }

    @Override
    public void syncTo(Quantity<Angle> ra, Quantity<Angle> dec) {
        this.ra = ra;
        this.dec = dec;
    }

    @Override
    public Quantity<Angle> getRightAscension() {
        return ra;
    }

    @Override
    public Quantity<Angle> getDeclination() {
        return dec;
    }

    @Override
    public void abort() {
        // No-op
    }

    @Override
    public Quantity<Angle> readValue() throws IOException {
        return dec; // Primary value is dec for now
    }

    @Override
    public void send(String command) throws IOException {
        // Process slewing command etc.
    }

    @Override
    public double[][] getImage() {
        return new double[1024][1024]; // Empty image
    }

    @Override
    @SuppressWarnings("unchecked")
    public Quantity<Angle>[] getTopLeftFOV() {
        return new Quantity[]{ra.subtract(Quantities.create(0.5, Units.DEGREE_ANGLE)), dec.add(Quantities.create(0.5, Units.DEGREE_ANGLE))};
    }

    @Override
    @SuppressWarnings("unchecked")
    public Quantity<Angle>[] getBottomRightFOV() {
        return new Quantity[]{ra.add(Quantities.create(0.5, Units.DEGREE_ANGLE)), dec.subtract(Quantities.create(0.5, Units.DEGREE_ANGLE))};
    }
    @Override
    protected void addCustomReadings(Map<String, String> readings) {
        readings.put("Right Ascension", ra.toString());
        readings.put("Declination", dec.toString());
    }
}
