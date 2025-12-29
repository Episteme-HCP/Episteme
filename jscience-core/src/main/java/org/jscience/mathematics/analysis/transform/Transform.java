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

package org.jscience.mathematics.analysis.transform;

import org.jscience.mathematics.analysis.Bijection;

/**
 * Represents a mathematical transform (e.g., Fourier, Laplace, Wavelet).
 * <p>
 * A transform maps a function or signal from one domain (e.g., time) to another
 * (e.g., frequency). Transforms are bijective, meaning they have well-defined
 * inverses (e.g., Fourier ↔ Inverse Fourier).
 * </p>
 * <p>
 * <b>Common transforms:</b>
 * <ul>
 * <li><b>Fourier Transform</b>: time domain ↔ frequency domain</li>
 * <li><b>Laplace Transform</b>: time domain ↔ s-domain</li>
 * <li><b>Wavelet Transform</b>: signal ↔ time-frequency representation</li>
 * <li><b>Z-Transform</b>: discrete-time ↔ z-domain</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Transform<D, C> extends Bijection<D, C> {

    /**
     * Returns the inverse transform.
     * <p>
     * For example, if this is the Fourier Transform (time → frequency),
     * the inverse is the Inverse Fourier Transform (frequency → time).
     * </p>
     * 
     * @return the inverse transform T⁻¹
     */
    @Override
    Transform<C, D> inverse();
}