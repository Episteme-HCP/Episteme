package org.jscience.measure.random;

import org.jscience.mathematics.algebraic.Matrix;
import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleSquareMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleSymmetricMatrix;

import java.io.Serializable;

/**
 * This class allows to generate random vectors with correlated components.
 * <p/>
 * <p>Random vectors with correlated components are built by combining
 * the uncorrelated components of another random vector in such a way
 * the resulting correlations are the ones specified by a positive
 * definite covariance matrix.</p>
 * <p/>
 * <p>Sometimes, the covariance matrix for a given simulation is not
 * strictly positive definite. This means that the correlations are
 * not all independant from each other. In this case, however, the non
 * strictly positive elements found during the Cholesky decomposition
 * of the covariance matrix should not be negative either, they
 * should be null. This implies that rather than computing <code>C =
 * L.Lt</code> where <code>C</code> is the covariance matrix and
 * <code>L</code> is a lower-triangular matrix, we compute <code>C =
 * B.Bt</code> where <code>B</code> is a rectangular matrix having
 * more rows than columns. The number of columns of <code>B</code> is
 * the rank of the covariance matrix, and it is the dimension of the
 * uncorrelated random vector that is needed to compute the component
 * of the correlated vector. This class does handle this situation
 * automatically.</p>
 *
 * @author L. Maisonobe
 * @version $Id: CorrelatedRandomVectorGenerator.java,v 1.2 2007-10-21 17:46:13 virtualcall Exp $
 */

public class CorrelatedRandomVectorGenerator
        implements Serializable, RandomVectorGenerator {

    /**
     * Mean vector.
     */
    private double[] mean;

    /**
     * Permutated Cholesky root of the covariance matrix.
     */
    private DoubleMatrix root;

    /**
     * Rank of the covariance matrix.
     */
    private int rank;

    /**
     * Underlying generator.
     */
    RandomGenerator generator;

    /**
     * Storage for the normalized vector.
     */
    private double[] normalized;

    /**
     * Storage for the random vector.
     */
    private double[] correlated;

    /**
     * Simple constructor.
     * <p>Build a correlated random vector generator from its mean
     * vector and covariance matrix.</p>
     *
     * @param mean       expected mean values for all components
     * @param covariance covariance matrix
     * @param generator  underlying generator for uncorrelated normalized
     *                   components
     * @throws IllegalArgumentException if there is a dimension
     *                                  mismatch between the mean vector and the covariance matrix
     * @throws NotPositiveDefiniteMatrixException
     *                                  if the
     *                                  covariance matrix is not strictly positive definite
     */
    public CorrelatedRandomVectorGenerator(double[] mean,
                                           DoubleSymmetricMatrix covariance,
                                           RandomGenerator generator)
            throws NotPositiveDefiniteMatrixException {

        int order = covariance.numRows();
        if (mean.length != order) {
            throw new IllegalArgumentException("dimension mismatch");
        }
        this.mean = mean;

        factorize(covariance);

        this.generator = generator;
        normalized = new double[rank];
        correlated = new double[order];

    }

    /**
     * Simple constructor.
     * <p>Build a null mean random correlated vector generator from its
     * covariance matrix.</p>
     *
     * @param covariance covariance matrix
     * @param generator  underlying generator for uncorrelated normalized
     *                   components
     * @throws NotPositiveDefiniteMatrixException
     *          if the
     *          covariance matrix is not strictly positive definite
     */
    public CorrelatedRandomVectorGenerator(DoubleSymmetricMatrix covariance,
                                           RandomGenerator generator)
            throws NotPositiveDefiniteMatrixException {

        int order = covariance.numRows();
        mean = new double[order];
        for (int i = 0; i < order; ++i) {
            mean[i] = 0;
        }

        factorize(covariance);

        this.generator = generator;
        normalized = new double[rank];
        correlated = new double[order];

    }

    /**
     * Get the root of the covariance matrix.
     * The root is the matrix <code>B</code> such that <code>B.Bt</code>
     * is equal to the covariance matrix
     *
     * @return root of the square matrix
     */
    public Matrix getRootMatrix() {
        return root;
    }

    /**
     * Get the underlying normalized components generator.
     *
     * @return underlying uncorrelated components generator
     */
    public RandomGenerator getGenerator() {
        return generator;
    }

    /**
     * Get the rank of the covariance matrix.
     * The rank is the number of independant rows in the covariance
     * matrix, it is also the number of columns of the rectangular
     * matrix of the factorization.
     *
     * @return rank of the square matrix.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Factorize the original square matrix.
     *
     * @param covariance covariance matrix
     * @throws NotPositiveDefiniteMatrixException
     *          if the
     *          covariance matrix is not strictly positive definite
     */
    private void factorize(DoubleSymmetricMatrix covariance)
            throws NotPositiveDefiniteMatrixException {

        int order = covariance.numRows();
        DoubleSymmetricMatrix c = (DoubleSymmetricMatrix) covariance.clone();
        DoubleSquareMatrix b = new DoubleSquareMatrix(order);

        int[] swap = new int[order];
        int[] index = new int[order];
        for (int i = 0; i < order; ++i) {
            index[i] = i;
        }

        rank = 0;
        for (boolean loop = true; loop;) {

            // find maximal diagonal element
            swap[rank] = rank;
            for (int i = rank + 1; i < order; ++i) {
                if (c.getPrimitiveElement(index[i], index[i])
                        > c.getPrimitiveElement(index[swap[i]], index[swap[i]])) {
                    swap[rank] = i;
                }
            }

            // swap elements
            if (swap[rank] != rank) {
                int tmp = index[rank];
                index[rank] = index[swap[rank]];
                index[swap[rank]] = tmp;
            }

            // check diagonal element
            if (c.getPrimitiveElement(index[rank], index[rank]) < 1.0e-12) {

                if (rank == 0) {
                    throw new NotPositiveDefiniteMatrixException();
                }

                // check remaining diagonal elements
                for (int i = rank; i < order; ++i) {
                    if (c.getPrimitiveElement(index[rank], index[rank]) < -1.0e-12) {
                        // there is at least one sufficiently negative diagonal element,
                        // the covariance matrix is wrong
                        throw new NotPositiveDefiniteMatrixException();
                    }
                }

                // all remaining diagonal elements are close to zero,
                // we consider we have found the rank of the covariance matrix
                ++rank;
                loop = false;

            } else {

                // transform the matrix
                double sqrt = Math.sqrt(c.getPrimitiveElement(index[rank], index[rank]));
                b.setElement(rank, rank, sqrt);
                double inverse = 1 / sqrt;
                for (int i = rank + 1; i < order; ++i) {
                    double e = inverse * c.getPrimitiveElement(index[i], index[rank]);
                    b.setElement(i, rank, e);
                    c.setElement(index[i], index[i],
                            c.getPrimitiveElement(index[i], index[i]) - e * e);
                    for (int j = rank + 1; j < i; ++j) {
                        double f = b.getPrimitiveElement(j, rank);
                        // NOTE: This takes advantage of the symmetrical setElement method.
                        c.setElement(index[i], index[j],
                                c.getPrimitiveElement(index[i], index[j])
                                        - e * f);
                    }
                }

                // prepare next iteration
                loop = ++rank < order;

            }

        }

        // build the root matrix
        root = new DoubleMatrix(order, rank);
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < rank; ++j) {
                root.setElement(swap[i], j, b.getPrimitiveElement(i, j));
            }
        }

    }

    /**
     * Generate a correlated random vector.
     *
     * @return a random vector as an array of double. The generator
     *         <em>will</em> reuse the same array for each call, in order to
     *         save the allocation time, so the user should keep a copy by
     *         himself if he needs so.
     */
    public double[] nextVector() {

        // generate uncorrelated vector
        for (int i = 0; i < rank; ++i) {
            normalized[i] = generator.nextDouble();
        }

        // compute correlated vector
        for (int i = 0; i < correlated.length; ++i) {
            correlated[i] = mean[i];
            for (int j = 0; j < rank; ++j) {
                correlated[i] += root.getPrimitiveElement(i, j) * normalized[j];
            }
        }

        return correlated;

    }

}
