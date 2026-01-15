package org.jscience.mathematics.algebraic;

/**
 * This class defines an interface for Matrices whose dimensions are equal,
 * thus leading to a square matrix.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface SquareMatrix extends Matrix {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSymmetric();
}
