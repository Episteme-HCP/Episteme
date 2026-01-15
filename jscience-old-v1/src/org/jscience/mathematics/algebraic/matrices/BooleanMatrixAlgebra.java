package org.jscience.mathematics.algebraic.matrices;

import org.jscience.mathematics.algebraic.algebras.Algebra;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.awt.*;

import java.util.Hashtable;


//todo: update this class when more BooleanMatrices class are supported
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public final class BooleanMatrixAlgebra implements Algebra, Ring {
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

    //private AbstractBooleanMatrix zero;
    /**
     * DOCUMENT ME!
     */
    private BooleanMatrix zero;

    //private AbstractBooleanSquareMatrix one;
    /**
     * DOCUMENT ME!
     */
    private BooleanMatrix one;

    /**
     * Creates a new BooleanMatrixAlgebra object.
     *
     * @param rows DOCUMENT ME!
     * @param cols DOCUMENT ME!
     */
    private BooleanMatrixAlgebra(int rows, int cols) {
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
    static BooleanMatrixAlgebra get(int rows, int cols) {
        Dimension dim = new Dimension(rows, cols);
        BooleanMatrixAlgebra algebra = (BooleanMatrixAlgebra) algebras.get(dim);

        if (algebra == null) {
            algebra = new BooleanMatrixAlgebra(rows, cols);
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
            one = identity(cols);
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
            zero = new BooleanMatrix(rows, cols);
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

    /**
     * DOCUMENT ME!
     *
     * @param cols DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private BooleanMatrix identity(int cols) {
        BooleanMatrix result = new BooleanMatrix(cols, cols);

        for (int i = 0; i < result.numColumns(); i++)
            result.setElement(i, i, true);

        return result;
    }
}
