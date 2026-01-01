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

package org.jscience.mathematics.structures.spaces;

import org.jscience.mathematics.structures.groups.AbelianGroup;
import org.jscience.mathematics.structures.rings.Ring;

/**
 * A module is a generalization of a vector space over a ring instead of a
 * field.
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * A left R-module M consists of:
 * <ul>
 * <li>An abelian group (M, +)</li>
 * <li>A ring R</li>
 * <li>A scalar multiplication R Ãƒâ€” M Ã¢â€ â€™ M satisfying:
 * <ul>
 * <li>r(x + y) = rx + ry</li>
 * <li>(r + s)x = rx + sx</li>
 * <li>(rs)x = r(sx)</li>
 * <li>1x = x (if R has unity)</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Module<M, R> extends AbelianGroup<M> {

    /**
     * Returns the ring of scalars for this module.
     * 
     * @return the scalar ring
     */
    Ring<R> getScalarRing();

    /**
     * Scalar multiplication (r Ãƒâ€” m).
     * 
     * @param scalar        the scalar r Ã¢Ë†Ë† R
     * @param moduleElement the module element m Ã¢Ë†Ë† M
     * @return r Ãƒâ€” m
     */
    M scale(R scalar, M moduleElement);
}

