/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.technical.backend.algorithms;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.geometry.Vector4D;
import static org.junit.jupiter.api.Assertions.*;

public class MulticoreMaxwellProviderTest {

    @Test
    public void testTensorAntisymmetry() {
        MulticoreMaxwellProvider provider = new MulticoreMaxwellProvider();
        Vector4D point = new Vector4D(1.0, 2.0, 3.0, 4.0);
        double[][] f = provider.computeTensor(point);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(f[i][j], -f[j][i], 1e-12, "Tensor must be antisymmetric at [" + i + "][" + j + "]");
            }
        }
    }

    @Test
    public void testNonZeroFieldNearSource() {
        MulticoreMaxwellProvider provider = new MulticoreMaxwellProvider();
        // Default source at origin, oscillating along Z axis
        Vector4D point = new Vector4D(10.0, 2.0, 0.0, 0.0); // 2 units away on X axis
        double[][] f = provider.computeTensor(point);

        boolean hasNonZero = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (Math.abs(f[i][j]) > 1e-15) {
                    hasNonZero = true;
                    break;
                }
            }
        }
        assertTrue(hasNonZero, "Field should be non-zero near source at t=10.0");
    }
}
