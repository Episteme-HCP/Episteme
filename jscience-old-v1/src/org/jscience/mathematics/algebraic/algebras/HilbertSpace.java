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

