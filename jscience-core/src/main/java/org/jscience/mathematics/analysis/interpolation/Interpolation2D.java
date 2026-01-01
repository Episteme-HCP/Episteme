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

package org.jscience.mathematics.analysis.interpolation;

import org.jscience.mathematics.numbers.real.Real;

/**
 * 2D Interpolation methods.
 * <p>
 * Methods for interpolating values on a 2D grid or scattered points.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Interpolation2D {

    /**
     * Bilinear interpolation on a unit square.
     * <p>
     * Interpolates value at (x, y) where 0 <= x, y <= 1.
     * Values at corners:
     * (0,0): v00
     * (1,0): v10
     * (0,1): v01
     * (1,1): v11
     * </p>
     */
    public static Real bilinear(Real x, Real y, Real v00, Real v10, Real v01, Real v11) {
        Real oneMinusX = Real.ONE.subtract(x);
        Real oneMinusY = Real.ONE.subtract(y);

        Real term1 = v00.multiply(oneMinusX).multiply(oneMinusY);
        Real term2 = v10.multiply(x).multiply(oneMinusY);
        Real term3 = v01.multiply(oneMinusX).multiply(y);
        Real term4 = v11.multiply(x).multiply(y);

        return term1.add(term2).add(term3).add(term4);
    }

    /**
     * Bilinear interpolation on an arbitrary rectangle.
     * 
     * @param x   target x coordinate
     * @param y   target y coordinate
     * @param x1  left x coordinate
     * @param x2  right x coordinate
     * @param y1  bottom y coordinate
     * @param y2  top y coordinate
     * @param q11 value at (x1, y1)
     * @param q21 value at (x2, y1)
     * @param q12 value at (x1, y2)
     * @param q22 value at (x2, y2)
     * @return interpolated value at (x, y)
     */
    public static Real bilinear(Real x, Real y,
            Real x1, Real x2, Real y1, Real y2,
            Real q11, Real q21, Real q12, Real q22) {

        Real x2_x = x2.subtract(x);
        Real x_x1 = x.subtract(x1);
        Real y2_y = y2.subtract(y);
        Real y_y1 = y.subtract(y1);

        Real denominator = x2.subtract(x1).multiply(y2.subtract(y1));

        Real term1 = q11.multiply(x2_x).multiply(y2_y);
        Real term2 = q21.multiply(x_x1).multiply(y2_y);
        Real term3 = q12.multiply(x2_x).multiply(y_y1);
        Real term4 = q22.multiply(x_x1).multiply(y_y1);

        return term1.add(term2).add(term3).add(term4).divide(denominator);
    }

    /**
     * Bicubic interpolation (simplified).
     * <p>
     * Requires 16 points (4x4 grid) surrounding the target point.
     * This implementation uses a simple convolution kernel (Catmull-Rom spline).
     * </p>
     * 
     * @param p 4x4 array of values. p[1][1] is top-left of the central cell.
     *          p[i][j] is value at grid point (i-1, j-1) relative to cell origin.
     * @param x normalized x coordinate within the cell (0 to 1)
     * @param y normalized y coordinate within the cell (0 to 1)
     * @return interpolated value
     */
    public static Real bicubic(Real[][] p, Real x, Real y) {
        Real[] arr = new Real[4];
        arr[0] = cubic(p[0], y);
        arr[1] = cubic(p[1], y);
        arr[2] = cubic(p[2], y);
        arr[3] = cubic(p[3], y);
        return cubic(arr, x);
    }

    /**
     * Cubic interpolation for 4 points.
     */
    private static Real cubic(Real[] p, Real x) {
        // Catmull-Rom spline
        // p[0] = y_{-1}, p[1] = y_0, p[2] = y_1, p[3] = y_2
        // x is between 0 and 1 (between p[1] and p[2])

        double v0 = p[0].doubleValue();
        double v1 = p[1].doubleValue();
        double v2 = p[2].doubleValue();
        double v3 = p[3].doubleValue();
        double t = x.doubleValue();

        double a0 = -0.5 * v0 + 1.5 * v1 - 1.5 * v2 + 0.5 * v3;
        double a1 = v0 - 2.5 * v1 + 2 * v2 - 0.5 * v3;
        double a2 = -0.5 * v0 + 0.5 * v2;
        double a3 = v1;

        return Real.of(a0 * t * t * t + a1 * t * t + a2 * t + a3);
    }
}

