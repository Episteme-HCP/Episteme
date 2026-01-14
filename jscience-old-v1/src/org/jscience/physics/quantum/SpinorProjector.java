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

package org.jscience.physics.quantum;

import org.jscience.mathematics.algebraic.matrices.ComplexVector;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The SpinorProjector class encapsulates the left-handed and right-handed
 * projection operators.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class SpinorProjector extends Projector {
    /** DOCUMENT ME! */
    private final static Complex[] ul = { Complex.ONE, Complex.ZERO };

    /** DOCUMENT ME! */
    private final static Complex[] ur = { Complex.ZERO, Complex.ONE };

    /** Left-handed projector (P<SUB>L</SUB>). */
    public final static SpinorProjector LEFT = new SpinorProjector(ul);

    /** Right-handed projector (P<SUB>R</SUB>). */
    public final static SpinorProjector RIGHT = new SpinorProjector(ur);

/**
     * Constructs a spinor projector from a ket vector.
     *
     * @param array a ket vector
     */
    private SpinorProjector(Complex[] array) {
        super(new KetVector(new ComplexVector(array)));
    }
}
