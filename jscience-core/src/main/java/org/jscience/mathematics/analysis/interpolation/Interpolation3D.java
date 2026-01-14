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

package org.jscience.mathematics.analysis.interpolation;

import org.jscience.mathematics.numbers.real.Real;

/**
 * 3D Interpolation methods.
 * <p>
 * Methods for interpolating values on a 3D grid.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Interpolation3D {

    /**
     * Trilinear interpolation on a unit cube.
     * <p>
     * Interpolates value at (x, y, z) where 0 <= x, y, z <= 1.
     * Values at corners cXYZ (e.g., c000 is at (0,0,0), c100 is at (1,0,0)).
     * </p>
     */
    public static Real trilinear(Real x, Real y, Real z,
            Real c000, Real c100, Real c010, Real c110,
            Real c001, Real c101, Real c011, Real c111) {

        Real oneMinusX = Real.ONE.subtract(x);

        // Interpolate along x
        Real c00 = c000.multiply(oneMinusX).add(c100.multiply(x));
        Real c01 = c001.multiply(oneMinusX).add(c101.multiply(x));
        Real c10 = c010.multiply(oneMinusX).add(c110.multiply(x));
        Real c11 = c011.multiply(oneMinusX).add(c111.multiply(x));

        // Interpolate along y
        Real oneMinusY = Real.ONE.subtract(y);
        Real c0 = c00.multiply(oneMinusY).add(c10.multiply(y));
        Real c1 = c01.multiply(oneMinusY).add(c11.multiply(y));

        // Interpolate along z
        Real oneMinusZ = Real.ONE.subtract(z);
        return c0.multiply(oneMinusZ).add(c1.multiply(z));
    }

    /**
     * Trilinear interpolation on an arbitrary rectangular prism.
     * 
     * @param x    target x
     * @param y    target y
     * @param z    target z
     * @param x0   x min
     * @param x1   x max
     * @param y0   y min
     * @param y1   y max
     * @param z0   z min
     * @param z1   z max
     * @param v000 value at (x0, y0, z0)
     * @param v100 value at (x1, y0, z0)
     * @param v010 value at (x0, y1, z0)
     * @param v110 value at (x1, y1, z0)
     * @param v001 value at (x0, y0, z1)
     * @param v101 value at (x1, y0, z1)
     * @param v011 value at (x0, y1, z1)
     * @param v111 value at (x1, y1, z1)
     * @return interpolated value
     */
    public static Real trilinear(Real x, Real y, Real z,
            Real x0, Real x1, Real y0, Real y1, Real z0, Real z1,
            Real v000, Real v100, Real v010, Real v110,
            Real v001, Real v101, Real v011, Real v111) {

        Real xd = x.subtract(x0).divide(x1.subtract(x0));
        Real yd = y.subtract(y0).divide(y1.subtract(y0));
        Real zd = z.subtract(z0).divide(z1.subtract(z0));

        return trilinear(xd, yd, zd, v000, v100, v010, v110, v001, v101, v011, v111);
    }
}

