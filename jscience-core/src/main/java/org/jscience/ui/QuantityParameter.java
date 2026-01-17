/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.ui;

import java.util.function.Consumer;
import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;

/**
 * Metadata for a physical quantity parameter (e.g. Length, Mass).
 * Handles unit conversion for UI display while maintaining type safety.
 *
 * @param <Q> the type of quantity
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuantityParameter<Q extends Quantity<Q>> extends Parameter<Quantity<Q>> {
    private final Quantity<Q> min;
    private final Quantity<Q> max;
    private final Quantity<Q> step;
    private final Unit<Q> unit;

    /**
     * Creates a new QuantityParameter.
     * 
     * @param name          display name
     * @param description   tooltip description
     * @param min           minimum value
     * @param max           maximum value
     * @param step          increment step
     * @param defaultValue  initial value
     * @param unit          the unit used for the slider/display logic
     * @param onValueChange callback
     */
    public QuantityParameter(String name, String description, 
                             Quantity<Q> min, Quantity<Q> max, Quantity<Q> step, 
                             Quantity<Q> defaultValue, Unit<Q> unit,
                             Consumer<Quantity<Q>> onValueChange) {
        super(name, description, defaultValue, onValueChange);
        this.min = min;
        this.max = max;
        this.step = step;
        this.unit = unit;
    }

    public Quantity<Q> getMin() {
        return min;
    }

    public Quantity<Q> getMax() {
        return max;
    }

    public Quantity<Q> getStep() {
        return step;
    }

    public Unit<Q> getUnit() {
        return unit;
    }
}
