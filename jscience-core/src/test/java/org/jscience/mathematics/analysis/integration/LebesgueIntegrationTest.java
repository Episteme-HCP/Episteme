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
package org.jscience.mathematics.analysis.integration;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

/**
 * Tests for LebesgueIntegration.
 */
public class LebesgueIntegrationTest {

    @Test
    public void testSimpleFunctionIntegration() {
        // Function takes value 2 on set of measure 3, and value 5 on set of measure 1
        // Integral = 2*3 + 5*1 = 11
        List<Real> values = Arrays.asList(Real.of(2), Real.of(5));
        List<Real> measures = Arrays.asList(Real.of(3), Real.of(1));

        Real result = LebesgueIntegration.integrateSimpleFunction(values, measures);
        assertEquals(11.0, result.doubleValue(), 1e-10);
    }

    @Test
    public void testMonteCarloIntegration() {
        // Integrate f(x, y) = x + y over [0, 1] x [0, 1]
        // Exact integral = 1

        Vector<Real> lower = new DenseVector<>(Arrays.asList(Real.ZERO, Real.ZERO),
                org.jscience.mathematics.sets.Reals.getInstance());
        Vector<Real> upper = new DenseVector<>(Arrays.asList(Real.ONE, Real.ONE),
                org.jscience.mathematics.sets.Reals.getInstance());

        Real result = LebesgueIntegration.monteCarlo(
                v -> v.get(0).add(v.get(1)),
                lower,
                upper,
                100000 // Enough samples for decent accuracy
        );

        // Monte Carlo is probabilistic, so use loose tolerance
        assertEquals(1.0, result.doubleValue(), 0.05);
    }
}
