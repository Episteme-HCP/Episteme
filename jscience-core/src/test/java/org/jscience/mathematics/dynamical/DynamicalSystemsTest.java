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
package org.jscience.mathematics.dynamical;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DynamicalSystemsTest {

    @Test
    public void testLogisticMap() {
        LogisticMap map = new LogisticMap(4.0);
        double[] state = { 0.2 };

        // x_1 = 4 * 0.2 * 0.8 = 0.64
        double[] next = map.map(state);
        assertEquals(0.64, next[0], 1e-9);

        assertEquals(1, map.dimensions());
    }

    @Test
    public void testHenonMap() {
        HenonMap map = new HenonMap(1.4, 0.3);
        double[] state = { 0.0, 0.0 };

        // x_1 = 1 - 1.4*0 + 0 = 1
        // y_1 = 0.3 * 0 = 0
        double[] next = map.map(state);
        assertEquals(1.0, next[0], 1e-9);
        assertEquals(0.0, next[1], 1e-9);

        assertEquals(2, map.dimensions());
    }
}
