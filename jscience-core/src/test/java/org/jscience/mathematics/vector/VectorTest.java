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

package org.jscience.mathematics.vector;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for Vector operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VectorTest {

    @Test
    public void testVectorCreation() {
        List<Real> data = Arrays.asList(Real.of(1), Real.of(2), Real.of(3));
        Vector<Real> v = DenseVector.of(data, Reals.getInstance());
        assertEquals(3, v.dimension());
        assertEquals(Real.of(1), v.get(0));
        assertEquals(Real.of(3), v.get(2));
    }

    @Test
    public void testDotProduct() {
        // v1 = [1, 2, 3], v2 = [4, 5, 6]
        // dot = 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        List<Real> data1 = Arrays.asList(Real.of(1), Real.of(2), Real.of(3));
        List<Real> data2 = Arrays.asList(Real.of(4), Real.of(5), Real.of(6));

        Vector<Real> v1 = DenseVector.of(data1, Reals.getInstance());
        Vector<Real> v2 = DenseVector.of(data2, Reals.getInstance());
        Real dot = v1.dot(v2);

        assertEquals(Real.of(32), dot);
    }

    @Test
    public void testNorm() {
        // v = [3, 4]
        // norm = sqrt(3^2 + 4^2) = sqrt(9 + 16) = 5
        List<Real> data = Arrays.asList(Real.of(3), Real.of(4));
        Vector<Real> v = DenseVector.of(data, Reals.getInstance());
        Real norm = v.norm();

        assertEquals(Real.of(5), norm);
    }
}
