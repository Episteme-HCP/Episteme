package org.jscience.measure.random;

import org.jscience.mathematics.algebraic.matrices.*;


/**
 * This is a useful collection of random related methods.
 *
 * @author Mark Hale
 */
public final class RandomUtils {
/**
     * Creates a new RandomUtils object.
     */
    private RandomUtils() {
    }

    /**
     * Creates a random generated vector.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AbstractDoubleVector randomVector(int size) {
        return new DoubleVector(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated vector.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AbstractComplexVector randomComplexVector(int size) {
        return new ComplexVector(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated square matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DoubleSquareMatrix randomSquareMatrix(int size) {
        return (DoubleSquareMatrix) new DoubleSquareMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated tridiagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DoubleTridiagonalMatrix randomTridiagonalMatrix(int size) {
        return (DoubleTridiagonalMatrix) new DoubleTridiagonalMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated diagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DoubleDiagonalMatrix randomDiagonalMatrix(int size) {
        return (DoubleDiagonalMatrix) new DoubleDiagonalMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated square matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComplexSquareMatrix randomComplexSquareMatrix(int size) {
        return (ComplexSquareMatrix) new ComplexSquareMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated tridiagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComplexTridiagonalMatrix randomComplexTridiagonalMatrix(
        int size) {
        return (ComplexTridiagonalMatrix) new ComplexTridiagonalMatrix(size).mapElements(RandomMap.MAP);
    }

    /**
     * Creates a random generated diagonal matrix.
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComplexDiagonalMatrix randomComplexDiagonalMatrix(int size) {
        return (ComplexDiagonalMatrix) new ComplexDiagonalMatrix(size).mapElements(RandomMap.MAP);
    }
}
