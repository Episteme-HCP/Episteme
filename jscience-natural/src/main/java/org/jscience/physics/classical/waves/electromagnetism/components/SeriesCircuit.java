/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.classical.waves.electromagnetism.components;

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a series connection of circuit components.
 * The total voltage is the sum of component voltages, and the current is the
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SeriesCircuit implements CircuitComponent {
    private final List<CircuitComponent> components = new ArrayList<>();

    public SeriesCircuit() {
    }

    public void addComponent(CircuitComponent component) {
        components.add(component);
    }

    public List<CircuitComponent> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        // In a series circuit, current is the same through all components.
        // For simplicity, we return the current of the first component if any, else
        // zero.
        if (components.isEmpty()) {
            return Real.ZERO;
        }
        // Assuming ideal components, compute current based on total resistance if all
        // are resistors.
        // Here we just delegate to the first component.
        return components.get(0).getCurrent(voltage, dt);
    }
}
