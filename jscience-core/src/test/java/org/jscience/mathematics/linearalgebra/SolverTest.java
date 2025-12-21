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
package org.jscience.mathematics.linearalgebra;

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.backends.CPUDenseLinearAlgebraProvider;
import org.jscience.mathematics.linearalgebra.backends.LinearAlgebraProvider;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolverTest {

    @Test
    public void testCircuitMatrix() {
        // Matrix from simulation log
        double[][] data = {
                { 0.002, 0.0, -0.002, 1.0, 1.0 },
                { 0.0, 1.0E-5, -1.0E-5, -1.0, 0.0 },
                { -0.002, -1.0E-5, 0.00201, 0.0, 0.0 },
                { -1.0, 1.0, 0.0, 0.0, 0.0 },
                { -1.0, 0.0, 0.0, 0.0, 0.0 }
        };

        double[] rhs = { 0.0, 0.0, 0.0, 10.0, 0.0 };

        int n = 5;
        List<List<Real>> rows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Real> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(Real.of(data[i][j]));
            }
            rows.add(row);
        }

        List<Real> bList = new ArrayList<>();
        for (double v : rhs)
            bList.add(Real.of(v));

        Matrix<Real> A = DenseMatrix.of(rows, Reals.getInstance());
        Vector<Real> b = new DenseVector<>(bList, Reals.getInstance());

        LinearAlgebraProvider<Real> provider = new CPUDenseLinearAlgebraProvider<>(Reals.getInstance());
        Vector<Real> x = provider.solve(A, b);

        System.out.println("Solution x: " + x);

        // Check V3 (Index 2)
        // Check V2 (Index 1) -> Should be 10.0
        // Check V1 (Index 0) -> Should be 0.0

        double v1 = x.get(0).doubleValue();
        double v2 = x.get(1).doubleValue();
        double v3 = x.get(2).doubleValue();

        assertEquals(0.0, v1, 1e-6, "V1 should be 0");
        assertEquals(10.0, v2, 1e-6, "V2 should be 10");
        // V3 approx 0.05
        assertEquals(0.04975, v3, 0.01, "V3 should be approx 0.05");
    }
}
