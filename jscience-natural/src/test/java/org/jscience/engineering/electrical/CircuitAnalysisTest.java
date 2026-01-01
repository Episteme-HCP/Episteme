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

package org.jscience.engineering.electrical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricPotential;
import org.jscience.measure.quantity.ElectricCurrent;
import org.jscience.measure.quantity.ElectricResistance;
import org.jscience.measure.quantity.Power;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Tests for CircuitAnalysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CircuitAnalysisTest {

    @Test
    public void testOhmsLawQuantity() {
        Quantity<ElectricCurrent> i = Quantities.create(2, Units.AMPERE);
        Quantity<ElectricResistance> r = Quantities.create(10, Units.OHM);

        Quantity<ElectricPotential> v = CircuitAnalysis.voltageQ(i, r);
        assertEquals(20.0, v.to(Units.VOLT).getValue().doubleValue(), 1e-9);
    }

    @Test
    public void testPowerQuantity() {
        Quantity<ElectricPotential> v = Quantities.create(12, Units.VOLT);
        Quantity<ElectricCurrent> i = Quantities.create(0.5, Units.AMPERE);

        Quantity<Power> p = CircuitAnalysis.powerQ(v, i);
        assertEquals(6.0, p.to(Units.WATT).getValue().doubleValue(), 1e-9);
    }

    @Test
    public void testParallelResistance() {
        // 10 || 10 = 5
        Real rTotal = CircuitAnalysis.resistanceParallel2(Real.of(10), Real.of(10));
        assertEquals(5.0, rTotal.doubleValue(), 1e-9);
    }
}


