package org.jscience.physics.quantum;

import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The Quantum math library. This class cannot be subclassed or
 * instantiated because all methods are static.
 *
 * @author Mark Hale
 * @version 1.5
 */
public final class QuantumMathUtils extends Object {
/**
     * Creates a new QuantumMathUtils object.
     */
    private QuantumMathUtils() {
    }

    // COMMUTATOR
    /**
     * Returns the commutator [A,B].
     *
     * @param A an operator
     * @param B an operator
     *
     * @return DOCUMENT ME!
     */
    public static Operator commutator(final Operator A, final Operator B) {
        return (A.multiply(B)).subtract(B.multiply(A));
    }

    // ANTICOMMUTATOR
    /**
     * Returns the anticommutator {A,B}.
     *
     * @param A an operator
     * @param B an operator
     *
     * @return DOCUMENT ME!
     */
    public static Operator anticommutator(final Operator A, final Operator B) {
        return (A.multiply(B)).add(B.multiply(A));
    }

    // EXPECTATION VALUES
    /**
     * Returns the expectation value.
     *
     * @param op an operator
     * @param ket a ket vector
     *
     * @return DOCUMENT ME!
     */
    public static Complex expectation(final Operator op, final KetVector ket) {
        return ket.toBraVector().multiply(op).multiply(ket);
    }

    /**
     * Returns the expectation value.
     *
     * @param dm a density matrix
     * @param op an operator
     *
     * @return DOCUMENT ME!
     */
    public static Complex expectation(final DensityMatrix dm, final Operator op) {
        return dm.multiply(op).trace();
    }

    // PROBABILITIES
    /**
     * Returns the probability.
     *
     * @param p a projector
     * @param ket a ket vector
     *
     * @return DOCUMENT ME!
     */
    public static Complex probability(final Projector p, final KetVector ket) {
        return ket.toBraVector().multiply(p).multiply(ket);
    }

    /**
     * Returns the probability.
     *
     * @param dm a density matrix
     * @param p a projector
     *
     * @return DOCUMENT ME!
     */
    public static Complex probability(final DensityMatrix dm, final Projector p) {
        return dm.multiply(p).trace();
    }
}
