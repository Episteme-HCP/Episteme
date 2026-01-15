package org.jscience.mathematics.algebraic;

import org.jscience.util.IllegalDimensionException;


/**
 * This class defines an interface for Banded Matrices (see interface).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class AbstractBandedMatrix extends AbstractSquareMatrix
    implements BandedMatrix {
    /**
     * DOCUMENT ME!
     */
    private int k1;

    /**
     * DOCUMENT ME!
     */
    private int k2;

/**
     * Constructs a matrix.
     *
     * @param rows DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     */
    public AbstractBandedMatrix(int rows, int k1, int k2) {
        super(rows);

        if ((k1 >= 0) && (k2 >= 0) && (k1 <= (rows - 1)) && (k2 <= (rows - 1))) {
            this.k1 = k1;
            this.k2 = k2;
        } else {
            throw new IllegalArgumentException(
                "k1 and k2 must be between 0 and rows - 1.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK1() {
        return k1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getK2() {
        return k2;
    }
}
