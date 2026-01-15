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
