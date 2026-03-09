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
import org.episteme.natural.device.sensors.HumidityProbe;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;
import java.util.Random;

/**
 * Simulated humidity probe.
 */
public class SimulatedHumidityProbe extends AbstractSimulatedSensor<Dimensionless> implements HumidityProbe {

    private final Random random = new Random();

    public SimulatedHumidityProbe(Identification id) {
        super(id);
        this.currentValue = Quantities.create(50.0, Units.PERCENT);
    }

    @Override
    public Quantity<Dimensionless> getRelativeHumidity() {
        try {
            return readValue();
        } catch (IOException e) {
            return (Quantity<Dimensionless>) currentValue;
        }
    }

    @Override
    public Quantity<Dimensionless> measure(Quantity<Dimensionless> actualHumidity) {
        // Simulate measurement noise around actual humidity
        double actual = actualHumidity.getValue().doubleValue();
        double measured = actual + (random.nextDouble() - 0.5) * 2.0;
        measured = Math.max(0, Math.min(100, measured));
        Quantity<Dimensionless> result = Quantities.create(measured, Units.PERCENT);
        setCurrentValue(result);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Quantity<Dimensionless> readValue() throws IOException {
        double v = 50.0 + (random.nextDouble() * 10 - 5);
        setCurrentValue(Quantities.create(v, Units.PERCENT));
        return (Quantity<Dimensionless>) currentValue;
    }
}
