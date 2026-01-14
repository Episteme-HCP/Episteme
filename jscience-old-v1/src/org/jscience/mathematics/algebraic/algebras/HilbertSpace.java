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

package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.matrices.ComplexVector;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Complex;

/**
 * The HilbertSpace class encapsulates Hilbert spaces.
 *
 * @author Mark Hale
 * @version 1.0
 * @planetmath HilbertSpace
 */

public class HilbertSpace extends Object implements BanachSpace {
    private int dim;
    private ComplexVector ZERO;

    /**
     * Constructs a Hilbert space.
     */
    public HilbertSpace(int n) {
        dim = n;
        ZERO = new ComplexVector(dim);
    }

    /**
     * Returns a vector from the Hilbert space.
     */
    public VectorSpace.Member getVector(Complex array[]) {
        return new ComplexVector(array);
    }

    /**
     * Returns the dimension.
     */
    public int dimension() {
        return dim;
    }

    /**
     * Returns the zero vector.
     */
    public AbelianGroup.Member zero() {
        return ZERO;
    }

    /**
     * Returns true if the vector is equal to zero.
     */
    public boolean isZero(AbelianGroup.Member v) {
        return ZERO.equals(v);
    }

    /**
     * Returns true if one vector is the negative of the other.
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return ZERO.equals(a.add(b));
    }

    /**
     * This interface defines a member of a Hilbert space.
     */
    public interface Member extends BanachSpace.Member {
        /**
         * The scalar product law.
         *
         * @param v a Hilbert space vector
         */
        Complex scalarProduct(Member v);
    }
}

