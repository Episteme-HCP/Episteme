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

import org.episteme.core.device.AbstractDevice;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Dimensionless;
import org.episteme.natural.device.sensors.Seismograph;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;
import java.util.Random;

/**
 * Simulated seismograph.
 */
public class SimulatedSeismograph extends AbstractDevice implements Seismograph {

    private final Random random = new Random();

    public SimulatedSeismograph(Identification id) {
        super(id);
        this.currentValue = Quantities.create(0.0, Units.ONE);
    }

    @Override
    public void connect() throws IOException {
        setStatus(Status.OPERATIONAL);
    }

    @Override
    public void disconnect() throws IOException {
        setStatus(Status.DISCONNECTED);
    }

    @Override
    public boolean isConnected() {
        return getDeviceStatus() == Status.OPERATIONAL;
    }

    @Override
    public Quantity<Dimensionless> readMagnitude() {
        double v = random.nextDouble() * 9.0;
        return Quantities.create(v, Units.ONE);
    }

    @Override
    public Quantity<Dimensionless> readValue() throws IOException {
        return readMagnitude();
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
