/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.analysis;

/**
 * A bijective function (one-to-one and onto mapping).
 * <p>
 * A bijection is a function f: D Ã¢â€ â€™ C where:
 * <ul>
 * <li><b>Injective</b> (one-to-one): f(x) = f(y) implies x = y</li>
 * <li><b>Surjective</b> (onto): For every c Ã¢Ë†Ë† C, there exists d Ã¢Ë†Ë† D such that
 * f(d) = c</li>
 * </ul>
 * Every bijection has an inverse function fÃ¢ÂÂ»Ã‚Â¹: C Ã¢â€ â€™ D.
 * </p>
 * <p>
 * <b>Examples of bijections:</b>
 * <ul>
 * <li>f(x) = 2x (Ã¢â€žÂ Ã¢â€ â€™ Ã¢â€žÂ)</li>
 * <li>f(x) = xÃ‚Â³ (Ã¢â€žÂ Ã¢â€ â€™ Ã¢â€žÂ)</li>
 * <li>exp(x) (Ã¢â€žÂ Ã¢â€ â€™ Ã¢â€žÂÃ¢ÂÂº), inverse is ln(x)</li>
 * <li>Fourier Transform (time Ã¢â€ â€™ frequency)</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Bijection<D, C> extends Function<D, C> {

    /**
     * Returns the inverse function fÃ¢ÂÂ»Ã‚Â¹.
     * <p>
     * For a bijection f: D Ã¢â€ â€™ C, the inverse fÃ¢ÂÂ»Ã‚Â¹: C Ã¢â€ â€™ D satisfies:
     * <ul>
     * <li>fÃ¢ÂÂ»Ã‚Â¹(f(x)) = x for all x Ã¢Ë†Ë† D</li>
     * <li>f(fÃ¢ÂÂ»Ã‚Â¹(y)) = y for all y Ã¢Ë†Ë† C</li>
     * </ul>
     * </p>
     * 
     * @return the inverse bijection
     */
    Bijection<C, D> inverse();
}


