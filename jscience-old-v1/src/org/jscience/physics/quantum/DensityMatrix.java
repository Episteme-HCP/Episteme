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

import org.jscience.mathematics.algebraic.matrices.AbstractComplexMatrix;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexSquareMatrix;
import org.jscience.mathematics.algebraic.matrices.ComplexSquareMatrix;


/**
 * The DensityMatrix class provides an object for encapsulating density
 * matrices.
 *
 * @author Mark Hale
 * @version 1.5
 */
public final class DensityMatrix extends Operator {
/**
     * Constructs a density matrix.
     *
     * @param kets  an array of ket vectors
     * @param probs the probabilities of being in the ket vector states.
     */
    public DensityMatrix(KetVector[] kets, double[] probs) {
        super(constructor(kets, probs));
    }

/**
     * Constructs a density matrix.
     *
     * @param projs an array of projectors
     * @param probs the probabilities of being in the projector states.
     */
    public DensityMatrix(Projector[] projs, double[] probs) {
        super(constructor(projs, probs));
    }

    /**
     * DOCUMENT ME!
     *
     * @param kets DOCUMENT ME!
     * @param probs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static AbstractComplexSquareMatrix constructor(KetVector[] kets,
        double[] probs) {
        AbstractComplexMatrix rep = (new Projector(kets[0])).getRepresentation()
                                     .scalarMultiply(probs[0]);

        for (int i = 1; i < kets.length; i++)
            rep = rep.add((new Projector(kets[i])).getRepresentation()
                           .scalarMultiply(probs[i]));

        return (ComplexSquareMatrix) rep;
    }

    /**
     * DOCUMENT ME!
     *
     * @param projs DOCUMENT ME!
     * @param probs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static AbstractComplexSquareMatrix constructor(Projector[] projs,
        double[] probs) {
        AbstractComplexMatrix rep = projs[0].getRepresentation()
                                            .scalarMultiply(probs[0]);

        for (int i = 1; i < projs.length; i++)
            rep = rep.add(projs[i].getRepresentation().scalarMultiply(probs[i]));

        return (AbstractComplexSquareMatrix) rep;
    }

    /**
     * Returns true if this density matrix is a pure state.
     *
     * @return DOCUMENT ME!
     */
    public boolean isPureState() {
        return representation.equals(representation.multiply(representation));
    }
}
