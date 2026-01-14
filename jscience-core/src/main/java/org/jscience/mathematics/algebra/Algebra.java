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

package org.jscience.mathematics.algebra;

import org.jscience.mathematics.structures.spaces.Module;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * An algebra over a field is a vector space equipped with a bilinear product.
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * An algebra A over a field K is a vector space equipped with a binary
 * operation
 * (x, y) Ã¢â€ Â¦ x Ã‚Â· y such that for all x, y, z Ã¢Ë†Ë† A and a, b Ã¢Ë†Ë† K:
 * <ul>
 * <li>(x + y) Ã‚Â· z = x Ã‚Â· z + y Ã‚Â· z</li>
 * <li>x Ã‚Â· (y + z) = x Ã‚Â· y + x Ã‚Â· z</li>
 * <li>(ax) Ã‚Â· (by) = (ab) (x Ã‚Â· y)</li>
 * </ul>
 * </p>
 *
 * <h2>Examples</h2>
 * <ul>
 * <li>Square matrices (MÃ¢â€šâ„¢(K))</li>
 * <li>Polynomials (K[x])</li>
 * <li>Complex numbers (as Ã¢â€žÂ-algebra)</li>
 * <li>Quaternions (as Ã¢â€žÂ-algebra)</li>
 * <li>Lie Algebras (with bracket [x,y] as product)</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Algebra<E, F> extends Module<E, F>, Ring<E> {

    /**
     * The bilinear product operation.
     * <p>
     * This is the same as the Ring multiplication, but explicitly
     * viewed as the algebra product.
     * </p>
     */
    @Override
    default E multiply(E a, E b) {
        return operate(a, b); // From Magma/Ring
    }
}

