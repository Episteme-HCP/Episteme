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
package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.numbers.complex.Complex;

/**
 * The Mandelbrot Set as a proper mathematical set.
 * <p>
 * The Mandelbrot set M ⊂ ℂ is defined as:
 * M = { c ∈ ℂ : the sequence z₀ = 0, zₙ₊₁ = zₙ² + c remains bounded }
 * </p>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>Benoit B. Mandelbrot, "The Fractal Geometry of Nature",
 * W.H. Freeman and Company, 1982</li>
 * <li>Adrien Douady and John H. Hubbard, "Étude dynamique des polynômes
 * complexes",
 * Publications Mathématiques d'Orsay, 1984-1985</li>
 * </ul>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MandelbrotSet implements org.jscience.mathematics.structures.sets.Set<Complex> {

    private final int maxIterations;
    private static final double ESCAPE_RADIUS_SQ = 4.0;

    public MandelbrotSet() {
        this(1000);
    }

    public MandelbrotSet(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public boolean contains(Complex c) {
        Complex z = Complex.ZERO;

        for (int i = 0; i < maxIterations; i++) {
            z = z.multiply(z).add(c);

            double r = z.getReal().doubleValue();
            double im = z.getImaginary().doubleValue();
            if (r * r + im * im > ESCAPE_RADIUS_SQ) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String description() {
        return "Mandelbrot Set M = { c ∈ ℂ : z_{n+1} = z_n² + c, z_0 = 0 remains bounded }";
    }

    public int escapeTime(Complex c) {
        Complex z = Complex.ZERO;

        for (int i = 0; i < maxIterations; i++) {
            z = z.multiply(z).add(c);

            double r = z.getReal().doubleValue();
            double im = z.getImaginary().doubleValue();
            if (r * r + im * im > ESCAPE_RADIUS_SQ) {
                return i;
            }
        }

        return maxIterations;
    }

    public int getMaxIterations() {
        return maxIterations;
    }
}