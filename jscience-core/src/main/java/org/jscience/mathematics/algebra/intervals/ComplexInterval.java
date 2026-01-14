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

package org.jscience.mathematics.algebra.intervals;

import org.jscience.mathematics.algebra.Interval;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Factory and convenience class for creating rectangular regions over Complex
 * numbers.
 * <p>
 * A "complex interval" is actually a rectangular region in the complex plane,
 * defined by intervals on both the real and imaginary axes:
 * [a, b] Ãƒâ€” [c, d]i represents all z = x + yi where x Ã¢Ë†Ë† [a,b] and y Ã¢Ë†Ë† [c,d].
 * </p>
 * <p>
 * These regions are useful for:
 * <ul>
 * <li>Complex analysis (domain specification)</li>
 * <li>Fractal computations (Mandelbrot/Julia sets)</li>
 * <li>Root finding in the complex plane</li>
 * <li>Contour integration bounds</li>
 * </ul>
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class ComplexInterval {

    private ComplexInterval() {
        // Utility class, no instantiation
    }

    /**
     * Creates a rectangular region in the complex plane.
     * <p>
     * Returns all z = x + yi where x Ã¢Ë†Ë† [realMin, realMax] and y Ã¢Ë†Ë† [imagMin,
     * imagMax].
     * </p>
     * 
     * @param realMin minimum real part
     * @param realMax maximum real part
     * @param imagMin minimum imaginary part
     * @param imagMax maximum imaginary part
     * @return a 2D interval representing the complex region
     */
    public static FieldIntervalND<Real> rectangle(Real realMin, Real realMax,
            Real imagMin, Real imagMax) {
        if (realMin.compareTo(realMax) > 0 || imagMin.compareTo(imagMax) > 0) {
            throw new IllegalArgumentException("min must be <= max for both axes");
        }

        Real[] min = new Real[] { realMin, imagMin };
        Real[] max = new Real[] { realMax, imagMax };

        return new FieldIntervalND<>(min, max,
                Real::add, Real::subtract, Real::divide, r -> Real.of(r));
    }

    /**
     * Creates a rectangular region from double values.
     */
    public static FieldIntervalND<Real> rectangle(double realMin, double realMax,
            double imagMin, double imagMax) {
        return rectangle(Real.of(realMin), Real.of(realMax),
                Real.of(imagMin), Real.of(imagMax));
    }

    /**
     * Creates a square region centered at a complex number.
     * 
     * @param center    the center of the square
     * @param halfWidth half the side length
     * @return a square region centered at the given point
     */
    public static FieldIntervalND<Real> square(Complex center, Real halfWidth) {
        Real x = Real.of(center.real());
        Real y = Real.of(center.imaginary());

        return rectangle(
                x.subtract(halfWidth), x.add(halfWidth),
                y.subtract(halfWidth), y.add(halfWidth));
    }

    /**
     * Creates a square region centered at origin with given half-width.
     */
    public static FieldIntervalND<Real> square(double halfWidth) {
        return square(Complex.ZERO, Real.of(halfWidth));
    }

    /**
     * Creates the unit square [0, 1] Ãƒâ€” [0, 1]i.
     */
    public static FieldIntervalND<Real> unitSquare() {
        return rectangle(0, 1, 0, 1);
    }

    /**
     * Creates the standard Mandelbrot viewing region [-2.5, 1] Ãƒâ€” [-1.5, 1.5]i.
     */
    public static FieldIntervalND<Real> mandelbrotRegion() {
        return rectangle(-2.5, 1.0, -1.5, 1.5);
    }

    /**
     * Checks if a complex number is inside the region.
     * 
     * @param region the complex region
     * @param z      the complex number to test
     * @return true if z is inside the region
     */
    public static boolean contains(Interval<Real> region, Complex z) {
        if (region.getDimension() != 2) {
            throw new IllegalArgumentException("Region must be 2D for complex containment");
        }

        Real x = Real.of(z.real());
        Real y = Real.of(z.imaginary());

        boolean xInRange = x.compareTo(region.getMin(0)) >= 0 &&
                x.compareTo(region.getMax(0)) <= 0;
        boolean yInRange = y.compareTo(region.getMin(1)) >= 0 &&
                y.compareTo(region.getMax(1)) <= 0;

        return xInRange && yInRange;
    }

    /**
     * Returns the center of the complex region.
     * 
     * @param region the complex region
     * @return the center as a Complex number
     */
    public static Complex center(Interval<Real> region) {
        if (region.getDimension() != 2) {
            throw new IllegalArgumentException("Region must be 2D");
        }

        Real centerReal = region.getMin(0).add(region.getMax(0)).divide(Real.TWO);
        Real centerImag = region.getMin(1).add(region.getMax(1)).divide(Real.TWO);

        return Complex.of(centerReal, centerImag);
    }

    /**
     * Returns the width (real axis extent) of the region.
     */
    public static Real width(Interval<Real> region) {
        return region.getMax(0).subtract(region.getMin(0));
    }

    /**
     * Returns the height (imaginary axis extent) of the region.
     */
    public static Real height(Interval<Real> region) {
        return region.getMax(1).subtract(region.getMin(1));
    }

    /**
     * Returns the area of the complex region.
     */
    public static Real area(Interval<Real> region) {
        return width(region).multiply(height(region));
    }

    /**
     * Creates a zoomed-in region centered at the same point.
     * 
     * @param region the original region
     * @param factor zoom factor (> 1 zooms in, < 1 zooms out)
     * @return a new region with reduced dimensions
     */
    public static FieldIntervalND<Real> zoom(Interval<Real> region, double factor) {
        Complex c = center(region);
        Real newHalfWidth = width(region).divide(Real.of(2 * factor));
        Real newHalfHeight = height(region).divide(Real.of(2 * factor));

        Real cx = Real.of(c.real());
        Real cy = Real.of(c.imaginary());

        return rectangle(
                cx.subtract(newHalfWidth), cx.add(newHalfWidth),
                cy.subtract(newHalfHeight), cy.add(newHalfHeight));
    }

    /**
     * Creates a translated region.
     * 
     * @param region the original region
     * @param delta  the translation vector as a Complex number
     * @return a new translated region
     */
    public static FieldIntervalND<Real> translate(Interval<Real> region, Complex delta) {
        Real dx = Real.of(delta.real());
        Real dy = Real.of(delta.imaginary());

        return rectangle(
                region.getMin(0).add(dx), region.getMax(0).add(dx),
                region.getMin(1).add(dy), region.getMax(1).add(dy));
    }
}

