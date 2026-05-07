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

package org.episteme.core.device;

import java.io.IOException;
import java.util.Optional;
import org.episteme.core.measure.Quantity;
import org.episteme.core.util.identity.Identification;

/**
 * Base implementation for sensor devices.
 *
 * @param <Q> the type of quantity produced by the sensor
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public abstract class AbstractSensor<Q extends Quantity<Q>> extends AbstractDevice implements Sensor<Q> {

    protected AbstractSensor(Identification identification) {
        super(identification);
    }

    @Override
    public Optional<Quantity<?>> getValue() {
        try {
            return Optional.ofNullable(readValue());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
