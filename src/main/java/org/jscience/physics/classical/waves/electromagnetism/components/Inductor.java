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
package org.jscience.physics.classical.waves.electromagnetism.components;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.classical.waves.electromagnetism.circuits.CircuitElement;

/**
 * Represents an ideal Inductor.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class Inductor extends CircuitElement implements CircuitComponent {

    private final Real inductance;
    private Real currentPrev;
    private double compResistance; // Companion model resistance

    public Inductor(double inductanceHenries) {
        super();
        this.inductance = Real.of(inductanceHenries);
        this.currentPrev = Real.ZERO;
    }

    public Inductor(Real inductance) {
        super();
        this.inductance = inductance;
        this.currentPrev = Real.ZERO;
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        Real dI = voltage.divide(inductance).multiply(dt);
        Real current = currentPrev.add(dI);
        currentPrev = current;
        return current;
    }

    public Real getInductance() {
        return inductance;
    }

    @Override
    public void reset() {
        super.reset();
        currentPrev = Real.ZERO;
    }

    @Override
    public void stamp() {
        // Trapezoidal companion model: L modeled as R=2L/dt in parallel with current
        // source
        if (circuit != null) {
            double dt = circuit.getTimeStep();
            compResistance = 2 * inductance.doubleValue() / dt;
            circuit.stampResistor(nodes[0], nodes[1], compResistance);
            circuit.stampCurrentSource(nodes[0], nodes[1], current);
        }
    }

    @Override
    public void doStep() {
        if (circuit != null) {
            // double dt = circuit.getTimeStep();
            double i = current + getVoltageDiff() / compResistance;
            circuit.stampCurrentSource(nodes[0], nodes[1], i);
        }
    }

    @Override
    public void calculateCurrent() {
        current = getVoltageDiff() / compResistance + current;
    }

    @Override
    public int getVoltageSourceCount() {
        return 0;
    }
}
