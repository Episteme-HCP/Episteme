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
package org.jscience.nativ.mathematics.ml;

import org.jscience.nativ.technical.algorithm.inference.NativeMulticoreMLProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification for Native ML Provider.
 */
public class MLProviderTest {

    @Test
    public void testKMeans() {
        NativeMulticoreMLProvider provider = new NativeMulticoreMLProvider();
        
        // Simple 2D data: 2 clusters, one around (-10,-10), one around (10,10)
        double[][] data = {
            {-10, -10}, {-9, -11}, {-11, -9},
            {10, 10}, {9, 11}, {11, 9}
        };

        int[] assignments = provider.kMeans(data, 2, 10);
        
        // Verify clustering: first 3 should be same, last 3 should be same, and different from each other
        assertEquals(assignments[0], assignments[1]);
        assertEquals(assignments[0], assignments[2]);
        assertEquals(assignments[3], assignments[4]);
        assertEquals(assignments[3], assignments[5]);
        assertNotEquals(assignments[0], assignments[3]);
    }
}
