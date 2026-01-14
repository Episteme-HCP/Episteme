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

package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.structures.groups.Magma;

/**
 * A Loop is a Quasigroup with an identity element.
 * <p>
 * A Quasigroup is a Magma where division is always possible (Latin Square
 * property).
 * A Loop adds the requirement of a neutral element (identity).
 * Unlike Groups, Loops are not required to be associative.
 * </p>
 *
 * <h2>Mathematical Definition</h2>
 * <p>
 * A loop (L, Ã‚Â·) is a set L with a binary operation Ã‚Â· such that:
 * <ul>
 * <li>For every a, b Ã¢Ë†Ë† L, there exist unique x, y Ã¢Ë†Ë† L such that a Ã‚Â· x = b and y
 * Ã‚Â· a = b.</li>
 * <li>There exists an identity element e Ã¢Ë†Ë† L such that a Ã‚Â· e = a and e Ã‚Â· a = a
 * for all a Ã¢Ë†Ë† L.</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Loop<E> extends Magma<E> {

    /**
     * Returns the identity element of the loop.
     * 
     * @return the identity element e
     */
    E identity();

    /**
     * Left division.
     * Returns the unique x such that a Ã‚Â· x = b.
     * Often denoted as a \ b.
     * 
     * @param a the left operand
     * @param b the result
     * @return x such that a Ã‚Â· x = b
     */
    E leftDivide(E a, E b);

    /**
     * Right division.
     * Returns the unique y such that y Ã‚Â· a = b.
     * Often denoted as b / a.
     * 
     * @param a the right operand
     * @param b the result
     * @return y such that y Ã‚Â· a = b
     */
    E rightDivide(E a, E b);
}

