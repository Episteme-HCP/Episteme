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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.physics.electricity;

import org.jscience.mathematics.number.Real;

/**
 * Represents an ideal Capacitor.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class Capacitor implements CircuitComponent {

    private final Real capacitance;
    private Real voltagePrev;

    public Capacitor(double capacitanceFarads) {
        this.capacitance = Real.of(capacitanceFarads);
        this.voltagePrev = Real.ZERO;
    }

    public Capacitor(Real capacitance) {
        this.capacitance = capacitance;
        this.voltagePrev = Real.ZERO;
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        // I = C * dV/dt
        // Backward difference: dV/dt ~ (V - V_prev) / dt
        Real dV = voltage.subtract(voltagePrev);
        Real current = capacitance.multiply(dV).divide(dt);

        // Update state
        voltagePrev = voltage;

        return current;
    }

    public Real getCapacitance() {
        return capacitance;
    }

    public void reset() {
        voltagePrev = Real.ZERO;
    }
}


