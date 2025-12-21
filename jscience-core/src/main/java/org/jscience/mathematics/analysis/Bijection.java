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
package org.jscience.mathematics.analysis;

/**
 * A bijective function (one-to-one and onto mapping).
 * <p>
 * A bijection is a function f: D → C where:
 * <ul>
 * <li><b>Injective</b> (one-to-one): f(x) = f(y) implies x = y</li>
 * <li><b>Surjective</b> (onto): For every c ∈ C, there exists d ∈ D such that
 * f(d) = c</li>
 * </ul>
 * Every bijection has an inverse function f⁻¹: C → D.
 * </p>
 * <p>
 * <b>Examples of bijections:</b>
 * <ul>
 * <li>f(x) = 2x (ℝ → ℝ)</li>
 * <li>f(x) = x³ (ℝ → ℝ)</li>
 * <li>exp(x) (ℝ → ℝ⁺), inverse is ln(x)</li>
 * <li>Fourier Transform (time → frequency)</li>
 * </ul>
 * </p>
 * 
 * @param <D> the domain type
 * @param <C> the codomain type
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Bijection<D, C> extends Function<D, C> {

    /**
     * Returns the inverse function f⁻¹.
     * <p>
     * For a bijection f: D → C, the inverse f⁻¹: C → D satisfies:
     * <ul>
     * <li>f⁻¹(f(x)) = x for all x ∈ D</li>
     * <li>f(f⁻¹(y)) = y for all y ∈ C</li>
     * </ul>
     * </p>
     * 
     * @return the inverse bijection
     */
    Bijection<C, D> inverse();
}