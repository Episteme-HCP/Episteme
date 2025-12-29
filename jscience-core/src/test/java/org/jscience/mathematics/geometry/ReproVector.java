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

package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

public class ReproVector {
    public static void main(String[] args) {
        try {
            System.out.println("ReproVector Start");

            Real r1 = Real.of(1.0);
            Real r2 = Real.of(1.0);
            System.out.println("r1: " + r1);
            System.out.println("r2: " + r2);

            Real sum = r1.add(r2);
            System.out.println("sum: " + sum);

            Real prod = r1.multiply(r2);
            System.out.println("prod: " + prod);

            Vector2D v = new Vector2D(1.0, 1.0);
            System.out.println("Vector v: " + v);

            Real norm = v.norm();
            System.out.println("Vector norm: " + norm);

            Vector2D normalized = v.normalize();
            System.out.println("Normalized: " + normalized);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
