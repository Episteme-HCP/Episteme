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
