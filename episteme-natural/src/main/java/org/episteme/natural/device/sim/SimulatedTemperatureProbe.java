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
import org.episteme.core.measure.quantity.Temperature;
import org.episteme.natural.device.sensors.TemperatureProbe;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;
import java.util.Random;

/**
 * Simulated temperature probe.
 */
public class SimulatedTemperatureProbe extends AbstractSimulatedSensor<Temperature> implements TemperatureProbe {

    private final ProbeType type;
    private final Quantity<Temperature> minTemp;
    private final Quantity<Temperature> maxTemp;
    private final Random random = new Random();

    public SimulatedTemperatureProbe(Identification id, ProbeType type, double min, double max) {
        super(id);
        this.type = type;
        this.minTemp = Quantities.create(min, Units.KELVIN);
        this.maxTemp = Quantities.create(max, Units.KELVIN);
        this.currentValue = Quantities.create(273.15, Units.KELVIN);
    }

    @Override
    public ProbeType getType() {
        return type;
    }

    @Override
    public Quantity<Temperature> getMinTemp() {
        return minTemp;
    }

    @Override
    public Quantity<Temperature> getMaxTemp() {
        return maxTemp;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Quantity<Temperature> readValue() throws IOException {
        double v = 273.15 + (random.nextDouble() * 20 - 10);
        setCurrentValue(Quantities.create(v, Units.KELVIN));
        return (Quantity<Temperature>) currentValue;
    }
}
