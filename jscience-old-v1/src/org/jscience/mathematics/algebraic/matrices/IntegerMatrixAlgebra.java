package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.algebras.Algebra;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.awt.*;

import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public final class IntegerMatrixAlgebra implements Algebra, Ring {
    /**
     * DOCUMENT ME!
     */
    private static final Hashtable algebras = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    private final int rows;

    /**
     * DOCUMENT ME!
     */
    private final int cols;

    /**
     * DOCUMENT ME!
     */
    private AbstractIntegerMatrix zero;

    /**
     * DOCUMENT ME!
     */
    private AbstractIntegerSquareMatrix one;

    /**
     * Creates a new IntegerMatrixAlgebra object.
     *
     * @param rows DOCUMENT ME!
     * @param cols DOCUMENT ME!
     */
    private IntegerMatrixAlgebra(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rows DOCUMENT ME!
     * @param cols DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static IntegerMatrixAlgebra get(int rows, int cols) {
        Dimension dim = new Dimension(rows, cols);
        IntegerMatrixAlgebra algebra = (IntegerMatrixAlgebra) algebras.get(dim);

        if (algebra == null) {
            algebra = new IntegerMatrixAlgebra(rows, cols);
            algebras.put(dim, algebra);
        }

        return algebra;
    }

    /**
     * Returns the (right) identity.
     *
     * @return DOCUMENT ME!
     */
    public Ring.Member one() {
        if (one == null) {
            one = IntegerDiagonalMatrix.identity(cols);
        }

        return one;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(Ring.Member r) {
        return one().equals(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbelianGroup.Member zero() {
        if (zero == null) {
            zero = new IntegerMatrix(rows, cols);
        }

        return zero;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(AbelianGroup.Member r) {
        return zero().equals(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNegative(AbelianGroup.Member a, AbelianGroup.Member b) {
        return zero().equals(a.add(b));
    }
}
