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
 * Represents a Memristor (memory resistor).
 * <p>
 * A memristor is a non-linear two-terminal electrical component relating
 * electric charge and magnetic flux linkage.
 * Its resistance (memristance) depends on the history of current that has
 * flowed through it.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Memristor implements CircuitComponent {

    private Real state; // Internal state variable (e.g., normalized width of doped region)
    private final Real rOn; // Minimum resistance (doped)
    private final Real rOff; // Maximum resistance (undoped)
    private final Real mobility; // Dopant mobility
    private final Real length; // Device length

    /**
     * Creates a new Memristor.
     * 
     * @param rOn          Minimum resistance (Ohms)
     * @param rOff         Maximum resistance (Ohms)
     * @param mobility     Dopant mobility (m^2/V/s)
     * @param length       Device length (m)
     * @param initialState Initial state (0 to 1)
     */
    public Memristor(double rOn, double rOff, double mobility, double length, double initialState) {
        this.rOn = Real.of(rOn);
        this.rOff = Real.of(rOff);
        this.mobility = Real.of(mobility);
        this.length = Real.of(length);
        this.state = Real.of(initialState);
    }

    /**
     * Calculates the current memristance based on the internal state.
     * M(w) = R_on * w + R_off * (1 - w)
     * 
     * @return Memristance in Ohms
     */
    public Real getMemristance() {
        return rOn.multiply(state).add(rOff.multiply(Real.ONE.subtract(state)));
    }

    /**
     * Updates the state of the memristor based on applied voltage and time step.
     * dw/dt = (mu_v * R_on / D^2) * i(t)
     * 
     * @param voltage Applied voltage (V)
     * @param dt      Time step (s)
     * @return The current flowing through the memristor
     */
    @Override
    public Real getCurrent(Real voltage, Real dt) {
        Real current = voltage.divide(getMemristance());

        // Simple linear ion drift model
        // dw/dt = (uv * Ron / D^2) * i(t)
        Real factor = mobility.multiply(rOn).divide(length.multiply(length));
        Real dw = factor.multiply(current).multiply(dt);

        state = state.add(dw);

        // Clamp state between 0 and 1
        if (state.compareTo(Real.ZERO) < 0)
            state = Real.ZERO;
        if (state.compareTo(Real.ONE) > 0)
            state = Real.ONE;

        return current;
    }

    public Real getState() {
        return state;
    }
}


