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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.classical.waves.electromagnetism.circuit.CircuitElement;

/**
 * Represents an ideal Inductor.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Inductor extends CircuitElement implements CircuitComponent {

    private final Real inductance;
    private Real currentPrev;
    private double compResistance; // Companion model resistance
    private double voltageLast;
    private double currentLast;

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
        Real c = currentPrev.add(dI);
        currentPrev = c;
        return c;
    }

    public Real getInductance() {
        return inductance;
    }

    @Override
    public void reset() {
        super.reset();
        currentPrev = Real.ZERO;
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
        // Trapezoidal companion model: L modeled as R=2L/dt in parallel with current
        // source
        if (circuit != null) {
            double dt = circuit.getTimeStep();
            compResistance = 2 * inductance.doubleValue() / dt;
            circuit.stampResistor(nodes[0], nodes[1], compResistance);
            // Initial source stamp (J = i_n + v_n/R)
            // At t=0, i=0, v=0 -> J=0.
        }
    }

    @Override
    public void doStep() {
        if (circuit != null) {
            // J = i_n + v_n / R_eq
            double curSourceValue = currentLast + voltageLast / compResistance;
            circuit.stampCurrentSource(nodes[0], nodes[1], curSourceValue);
        }
    }

    @Override
    public void calculateCurrent() {
        // i_{n+1} = v_{n+1}/R_eq + J
        double vDiff = getVoltageDiff();
        double j = currentLast + voltageLast / compResistance;
        current = vDiff / compResistance + j;
        // System.out.println("IND: V=" + vDiff + " I_new=" + current);
    }

    @Override
    public int getVoltageSourceCount() {
        return 0;
    }
}


