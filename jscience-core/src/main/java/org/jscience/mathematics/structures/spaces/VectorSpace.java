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

package org.jscience.mathematics.structures.spaces;

import org.jscience.mathematics.structures.rings.Field;

/**
 * A vector space is a module over a field.
 * <p>
 * This is the central structure of linear algebra.
 * </p>
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * A vector space V over a field F is a module where the scalar ring is a field.
 * Elements of V are called vectors, elements of F are called scalars.
 * </p>
 *
 * <h2>Examples</h2>
 * <ul>
 * <li>Ã¢â€žÂÃ¢ÂÂ¿ (Euclidean space)</li>
 * <li>Ã¢â€žâ€šÃ¢ÂÂ¿ (Complex coordinate space)</li>
 * <li>Function spaces (e.g., LÃ‚Â²)</li>
 * <li>Polynomial spaces</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface VectorSpace<V, F> extends Module<V, F> {

    /**
     * Returns the field of scalars.
     * 
     * @return the scalar field
     */
    default Field<F> getScalarField() {
        return (Field<F>) getScalarRing();
    }

    /**
     * Returns the dimension of the vector space (if finite).
     * 
     * @return the dimension, or -1 if infinite/unknown
     */
    default int dimension() {
        return -1;
    }
}

