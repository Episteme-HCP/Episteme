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
import org.episteme.core.measure.quantity.Pressure;
import org.episteme.natural.device.sensors.PressureGauge;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;
import java.util.Random;

/**
 * Simulated pressure gauge.
 */
public class SimulatedPressureGauge extends AbstractDevice implements PressureGauge {

    private final GaugeType type;
    private final Quantity<Pressure> minPressure;
    private final Quantity<Pressure> maxPressure;
    private final Random random = new Random();

    public SimulatedPressureGauge(Identification id, GaugeType type, double min, double max) {
        super(id);
        this.type = type;
        this.minPressure = Quantities.create(min, Units.PASCAL);
        this.maxPressure = Quantities.create(max, Units.PASCAL);
        this.currentValue = Quantities.create(101325, Units.PASCAL);
    }

    @Override
    public GaugeType getType() {
        return type;
    }

    @Override
    public Quantity<Pressure> getMinPressure() {
        return minPressure;
    }

    @Override
    public Quantity<Pressure> getMaxPressure() {
        return maxPressure;
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
    @SuppressWarnings("unchecked")
    public Quantity<Pressure> readValue() throws IOException {
        double v = 101325 + (random.nextDouble() * 1000 - 500);
        setCurrentValue(Quantities.create(v, Units.PASCAL));
        return (Quantity<Pressure>) currentValue;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
