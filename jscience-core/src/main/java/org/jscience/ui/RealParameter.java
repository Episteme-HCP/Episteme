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
import org.jscience.mathematics.numbers.real.Real;

/**
 * Metadata for a Real number parameter with bounds.
 * Allows binding UI controls (double-based) to Real-based scientific models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RealParameter extends Parameter<Real> {
    private final Real min;
    private final Real max;
    private final Real step;

    public RealParameter(String name, String description, Real min, Real max, Real step, Real defaultValue,
            Consumer<Real> onValueChange) {
        super(name, description, defaultValue, onValueChange);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public RealParameter(String name, String description, double min, double max, double step, double defaultValue,
            Consumer<Real> onValueChange) {
        this(name, description, Real.of(min), Real.of(max), Real.of(step), Real.of(defaultValue), onValueChange);
    }

    public Real getMin() {
        return min;
    }

    public Real getMax() {
        return max;
    }

    public Real getStep() {
        return step;
    }
}
