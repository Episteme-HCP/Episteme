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
import org.episteme.natural.device.sensors.Multimeter;
import org.episteme.core.util.identity.Identification;

import java.io.IOException;

/**
 * Simulated implementation of Multimeter.
 */
public class SimulatedMultimeter extends AbstractSimulatedSensor<Dimensionless> implements Multimeter {

    private Function function = Function.DC_VOLTAGE;

    public SimulatedMultimeter(Identification id) {
        super(id);
        this.currentValue = Quantities.create(0.0, Units.ONE);
    }

    @Override
    public Function getFunction() {
        return function;
    }

    @Override
    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Quantity<Dimensionless> readValue() throws IOException {
        return (Quantity<Dimensionless>) currentValue;
    }

    /**
     * Mocks probing a value.
     */
    public void probe(Quantity<Dimensionless> value) {
        setCurrentValue(value);
    }
}
