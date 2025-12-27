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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.classical.waves.electromagnetism.circuits.CircuitElement;

/**
 * Represents an ideal Capacitor.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Capacitor extends CircuitElement implements CircuitComponent {

    private final Real capacitance;
    private double voltageLast;
    private double currentLast;
    private double compResistance;

    public Capacitor(double capacitanceFarads) {
        super();
        this.capacitance = Real.of(capacitanceFarads);
    }

    public Capacitor(Real capacitance) {
        super();
        this.capacitance = capacitance;
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        // Not used by MNA solver directly
        return Real.of(current);
    }

    public Real getCapacitance() {
        return capacitance;
    }

    @Override
    public void reset() {
        super.reset();
        voltageLast = 0;
        currentLast = 0;
    }

    @Override
    public void startIteration() {
        voltageLast = getVoltageDiff();
        currentLast = current;
    }

    @Override
    public void stamp() {
        if (circuit != null) {
            double dt = circuit.getTimeStep();
            compResistance = dt / (2 * capacitance.doubleValue());
            circuit.stampResistor(nodes[0], nodes[1], compResistance);
        }
    }

    @Override
    public void doStep() {
        if (circuit != null) {
            // I_equivalent = - (I_old + V_old / R_eq)
            double curSourceValue = -(currentLast + voltageLast / compResistance);
            circuit.stampCurrentSource(nodes[0], nodes[1], curSourceValue);
        }
    }

    @Override
    public void calculateCurrent() {
        // I_new = (V_new - V_old) / R_eq - I_old
        double vDiff = getVoltageDiff();
        current = (vDiff - voltageLast) / compResistance - currentLast;
    }
}
