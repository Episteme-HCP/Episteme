package org.jscience.mathematics.algebraic.groups;

import org.jscience.mathematics.algebraic.matrices.AbstractComplexMatrix;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexSquareMatrix;
import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.ComplexDiagonalMatrix;
import org.jscience.mathematics.algebraic.numbers.Complex;

/**
 * The LieGroup class provides an encapsulation for Lie groups.
 * Elements are represented by complex matrices, and are limited
 * to being near the identity.
 *
 * @author Mark Hale
 * @version 1.3
 * @planetmath LieGroup
 */

//we should implement Group here!
public class LieGroup extends Object {
    private AbstractComplexSquareMatrix generators[];
    private AbstractComplexSquareMatrix identityMatrix;

    /**
     * Constructs a Lie group from a Lie algebra.
     *
     * @param gens the group generators
     */
    public LieGroup(AbstractComplexSquareMatrix gens[]) {
        generators = gens;
        identityMatrix = ComplexDiagonalMatrix.identity(generators[0].numRows());
    }

    /**
     * Returns the dimension of the group.
     */
    public final int dimension() {
        return generators.length;
    }

    /**
     * Returns an element near the identity.
     *
     * @param v a small element from the Lie algebra
     */
    public AbstractComplexSquareMatrix getElement(AbstractDoubleVector v) {
        if (generators.length != v.getDimension())
            throw new IllegalArgumentException("The vector should match the generators.");
        AbstractComplexMatrix phase = generators[0].scalarMultiply(v.getPrimitiveElement(0));
        for (int i = 1; i < generators.length; i++)
            phase = phase.add(generators[i].scalarMultiply(v.getPrimitiveElement(i)));
        return (AbstractComplexSquareMatrix) identityMatrix.add(phase.scalarMultiply(Complex.I));
    }

    /**
     * Returns the identity element.
     */
    public AbstractComplexSquareMatrix identity() {
        return identityMatrix;
    }

    /**
     * Returns true if the element is the identity element of this group.
     *
     * @param a a group element
     */
    public final boolean isIdentity(AbstractComplexSquareMatrix a) {
        return identityMatrix.equals(a);
    }

    /**
     * Returns true if one element is the inverse of the other.
     *
     * @param a a group element
     * @param b a group element
     */
    public final boolean isInverse(AbstractComplexSquareMatrix a, AbstractComplexSquareMatrix b) {
        return isIdentity(a.multiply(b));
    }
}

