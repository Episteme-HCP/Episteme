/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * A simple circuit that aggregates multiple {@link CircuitComponent}s.
 * It provides basic methods to add components and compute total voltage
 * and current assuming ideal connections. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class SimpleCircuit implements CircuitComponent {
    private final List<CircuitComponent> components = new ArrayList<>();

    public SimpleCircuit() {
    }

    public void addComponent(CircuitComponent component) {
        components.add(component);
    }

    public List<CircuitComponent> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        // Sum currents of all components for the given voltage.
        Real total = Real.ZERO;
        for (CircuitComponent c : components) {
            total = total.add(c.getCurrent(voltage, dt));
        }
        return total;
    }
}
