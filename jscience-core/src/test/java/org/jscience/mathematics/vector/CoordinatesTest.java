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
package org.jscience.mathematics.vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.mathematics.linearalgebra.Vector;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import java.util.Arrays;

/**
 * Tests for Coordinate Systems (Vectors/Matrices).
 */
public class CoordinatesTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testVectorAddition() {
        Vector<Real> v1 = DenseVector.of(Arrays.asList(Real.of(1.0), Real.of(2.0), Real.of(3.0)), Reals.getInstance());
        Vector<Real> v2 = DenseVector.of(Arrays.asList(Real.of(4.0), Real.of(5.0), Real.of(6.0)), Reals.getInstance());

        Vector<Real> sum = v1.add(v2);

        assertEquals(5.0, sum.get(0).doubleValue(), DELTA);
        assertEquals(7.0, sum.get(1).doubleValue(), DELTA);
        assertEquals(9.0, sum.get(2).doubleValue(), DELTA);
    }

    @Test
    public void testVectorScaling() {
        Vector<Real> v1 = DenseVector.of(Arrays.asList(Real.of(1.0), Real.of(2.0)), Reals.getInstance());
        Vector<Real> scaled = v1.multiply(Real.of(2.0));

        assertEquals(2.0, scaled.get(0).doubleValue(), DELTA);
        assertEquals(4.0, scaled.get(1).doubleValue(), DELTA);
    }
}
