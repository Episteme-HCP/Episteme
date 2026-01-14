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

package org.jscience.mathematics.analysis.chaos;

import org.jscience.mathematics.numbers.complex.Complex;

/**
 * Julia Set as a proper mathematical set.
 * <p>
 * For a given complex parameter c, the Julia set J_c Ã¢Å â€š Ã¢â€žâ€š is defined as:
 * J_c = { z Ã¢Ë†Ë† Ã¢â€žâ€š : the sequence zÃ¢â€šâ‚¬ = z, zÃ¢â€šâ„¢Ã¢â€šÅ Ã¢â€šÂ = zÃ¢â€šâ„¢Ã‚Â² + c remains bounded }
 * </p>
 *
 * <h2>References</h2>
 * <ul>
 * <li>Gaston Julia, "MÃƒÂ©moire sur l'itÃƒÂ©ration des fonctions rationnelles",
 * Journal de MathÃƒÂ©matiques Pures et AppliquÃƒÂ©es, Vol. 8, 1918, pp. 47-245</li>
 * <li>Benoit B. Mandelbrot, "The Fractal Geometry of Nature",
 * W.H. Freeman and Company, 1982 (popularized Julia sets)</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JuliaSet implements org.jscience.mathematics.structures.sets.Set<Complex> {

    private final Complex c;
    private final int maxIterations;
    private static final double ESCAPE_RADIUS_SQ = 4.0;

    public JuliaSet(Complex c) {
        this(c, 1000);
    }

    public JuliaSet(Complex c, int maxIterations) {
        this.c = c;
        this.maxIterations = maxIterations;
    }

    @Override
    public boolean contains(Complex z) {
        Complex current = z;

        for (int i = 0; i < maxIterations; i++) {
            current = current.multiply(current).add(c);

            double r = current.getReal().doubleValue();
            double im = current.getImaginary().doubleValue();
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
        return String.format("Julia Set J_c for c = %s", c);
    }

    public int escapeTime(Complex z) {
        Complex current = z;

        for (int i = 0; i < maxIterations; i++) {
            current = current.multiply(current).add(c);

            double r = current.getReal().doubleValue();
            double im = current.getImaginary().doubleValue();
            if (r * r + im * im > ESCAPE_RADIUS_SQ) {
                return i;
            }
        }

        return maxIterations;
    }

    public Complex getParameter() {
        return c;
    }

    public int getMaxIterations() {
        return maxIterations;
    }
}

