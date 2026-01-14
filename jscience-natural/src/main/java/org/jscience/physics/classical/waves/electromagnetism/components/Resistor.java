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
 * Represents an ideal Resistor.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Resistor extends CircuitElement implements CircuitComponent {

    private final Real resistance;

    public Resistor(double resistanceOhms) {
        super();
        this.resistance = Real.of(resistanceOhms);
    }

    public Resistor(Real resistance) {
        super();
        this.resistance = resistance;
    }

    @Override
    public Real getCurrent(Real voltage, Real dt) {
        return voltage.divide(resistance);
    }

    public Real getResistance() {
        return resistance;
    }

    @Override
    public void stamp() {
        // Stamp conductance: G = 1/R
        double r = resistance.doubleValue();
        if (circuit != null && r > 0) {
            circuit.stampResistor(nodes[0], nodes[1], r);
        }
    }

    @Override
    public void calculateCurrent() {
        current = getVoltageDiff() / resistance.doubleValue();
    }
}


