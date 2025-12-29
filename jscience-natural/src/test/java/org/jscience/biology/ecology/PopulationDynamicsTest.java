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

package org.jscience.biology.ecology;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Dimensionless;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Tests for PopulationDynamics.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PopulationDynamicsTest {

    @Test
    public void testExponentialGrowth() {
        Real p0 = Real.of(100);
        Real r = Real.of(0.1);
        Real t = Real.of(10);
        double expected = 100 * Math.exp(1); // e^1 ~ 2.718 -> 271.8

        Real result = PopulationDynamics.exponentialGrowth(p0, r, t);
        assertEquals(expected, result.doubleValue(), 1e-5);
    }

    @Test
    public void testExponentialGrowthQuantity() {
        Quantity<Dimensionless> p0 = Quantities.create(100, Units.ONE);
        Quantity<Frequency> r = Quantities.create(0.1, Units.HERTZ); // 0.1 / s
        Quantity<Time> t = Quantities.create(10, Units.SECOND);

        Quantity<Dimensionless> res = PopulationDynamics.exponentialGrowth(p0, r, t);
        assertEquals(100 * Math.exp(1), res.getValue().doubleValue(), 1e-5);
    }

    @Test
    public void testLogisticGrowth() {
        Real p0 = Real.of(10);
        Real K = Real.of(100);
        Real r = Real.of(0.5);
        Real t = Real.of(2);

        Real res = PopulationDynamics.logisticGrowth(p0, r, K, t);
        // P(t) = 100 / (1 + (9)*e^-1) = 100 / (1 + 9*0.3678) = 100 / (1+3.31) =
        // 100/4.31 = 23.2
        double expected = 100.0 / (1.0 + ((100.0 - 10.0) / 10.0) * Math.exp(-0.5 * 2.0));
        assertEquals(expected, res.doubleValue(), 1e-5);
    }

    @Test
    public void testLogisticGrowthQuantity() {
        Quantity<Dimensionless> p0 = Quantities.create(10, Units.ONE);
        Quantity<Dimensionless> K = Quantities.create(100, Units.ONE);
        Quantity<Frequency> r = Quantities.create(0.5, Units.HERTZ);
        Quantity<Time> t = Quantities.create(2, Units.SECOND);

        Quantity<Dimensionless> res = PopulationDynamics.logisticGrowth(p0, r, K, t);
        double expected = 100.0 / (1.0 + 9.0 * Math.exp(-1.0));
        assertEquals(expected, res.getValue().doubleValue(), 1e-5);
    }
}
