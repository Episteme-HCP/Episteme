package org.jscience.mathematics.analysis.estimation;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;
import org.jscience.mathematics.algebraic.matrices.DoubleSquareMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;
import org.jscience.mathematics.algebraic.matrices.LinearMathUtils;

import java.io.Serializable;

/**
 * This class implements a solver for estimation problems.
 * <p/>
 * <p/>
 * <p>This class solves estimation problems using a weighted least
 * squares criterion on the measurement residuals. It uses a
 * Gauss-Newton algorithm.</p>
 *
 * @author L. Maisonobe
 * @version $Id: LeastSquaresEstimator.java,v 1.2 2007-10-21 17:38:12 virtualcall Exp $
 */

//also see org.jscience.mathematics.analysis.polynomials.PolynomialMathUtils
public class LeastSquaresEstimator
        implements Estimator, Serializable {
    /**
     * Simple constructor.
     * <p/>
     * <p/>
     * <p>This constructor build an estimator and store its convergence
     * characteristics.</p>
     * <p/>
     * <p/>
     * <p>An estimator is considered to have converge whenever either
     * the criterion goes below a physical threshold under which
     * improvement are considered useless or when the algorithm is
     * unable to improve it (even if it is still high). The first
     * condition that is met stops the iterations.</p>
     * <p/>
     * <p/>
     * <p>The fact an estimator has converged does not mean that the
     * model accurately fit the measurements. It only means no better
     * solution can be found, it does not mean this one is good. Such an
     * analysis belong to the caller.</p>
     * <p/>
     * <p/>
     * <p>If neither condition is fulfilled before a given number of
     * iterations, the algorithm is considered to have failed and an
     * {@link EstimationException}will be thrown.</p>
     *
     * @param maxIterations        maximum number of iterations allowed
     * @param convergence          criterion threshold below which we do not need
     *                             to improve the criterion anymore
     * @param steadyStateThreshold steady state detection threshold, the
     *                             problem has converged has reached a steady state if
     *                             <code>Math.abs (Jn - Jn-1) < Jn * convergence</code>, where
     *                             <code>Jn</code> and <code>Jn-1</code> are the current and
     *                             preceding criterion value (square sum of the weighted residuals
     *                             of considered measurements).
     * @param epsilon              threshold under which the matrix of the linearized
     *                             problem is considered singular (see {@link
     *                             org.jscience.mathematics.analysis.linalg.SquareMatrix#solve
     *                             SquareMatrix.solve}).
     */
    public LeastSquaresEstimator(int maxIterations,
                                 double convergence,
                                 double steadyStateThreshold,
                                 double epsilon) {
        this.maxIterations = maxIterations;
        this.steadyStateThreshold = steadyStateThreshold;
        this.convergence = convergence;
        this.epsilon = epsilon;
    }

    /**
     * Solve an estimation problem using a least squares criterion.
     * <p/>
     * <p/>
     * <p>This method set the unbound parameters of the given problem
     * starting from their current values through several iterations. At
     * each step, the unbound parameters are changed in order to
     * minimize a weighted least square criterion based on the
     * measurements of the problem.</p>
     * <p/>
     * <p/>
     * <p>The iterations are stopped either when the criterion goes
     * below a physical threshold under which improvement are considered
     * useless or when the algorithm is unable to improve it (even if it
     * is still high). The first condition that is met stops the
     * iterations. If the convergence it nos reached before the maximum
     * number of iterations, an {@link EstimationException} is
     * thrown.</p>
     *
     * @param problem estimation problem to solve
     * @throws EstimationException if the problem cannot be solved
     * @see EstimationProblem
     */
    public void estimate(EstimationProblem problem)
            throws EstimationException {
        int iterations = 0;
        double previous = 0.0;
        double current = 0.0;

        // iterate until convergence is reached
        do {

            if (++iterations > maxIterations) {
                throw new EstimationException("unable to converge in "
                        + maxIterations + " iterations");
            }

            // perform one iteration
            linearEstimate(problem);

            previous = current;
            current = evaluateCriterion(problem);

        } while ((iterations < 2)
                || (Math.abs(previous - current) > (current * steadyStateThreshold)
                && (Math.abs(current) > convergence)));

    }

    /**
     * Estimate the solution of a linear least square problem.
     * <p/>
     * <p/>
     * <p>The Gauss-Newton algorithm is iterative. Each iteration
     * consist in solving a linearized least square problem. Several
     * iterations are needed for general problems since the
     * linearization is only an approximation of the problem
     * behaviour. However, for linear problems one iteration is enough
     * to get the solution. This method is provided in the public
     * interface in order to handle more efficiently these linear
     * problems.</p>
     *
     * @param problem estimation problem to solve
     */
    public void linearEstimate(EstimationProblem problem) {

        EstimatedParameter[] parameters = problem.getUnboundParameters();
        WeightedMeasurement[] measurements = problem.getMeasurements();

        // build the linear problem (the a matrix is really a symmetrical matrix)
        DoubleSquareMatrix a = new DoubleSquareMatrix(parameters.length);
        DoubleVector b = new DoubleVector(parameters.length);
        for (int i = 0; i < measurements.length; ++i) {
            if (!measurements[i].isIgnored()) {
                double weight = measurements[i].getWeight();
                double residual = measurements[i].getResidual();

                // compute the normal equation
                double[] grad = new double[parameters.length];
                for (int j = 0; j < parameters.length; ++j) {
                    grad[j] = measurements[i].getPartial(parameters[j]);
                }

                // update the matrices
                for (int j = 0; j < parameters.length; ++j) {
                    for (int k = 0; k < parameters.length; ++k) {
                        a.setElement(j, k,
                                a.getPrimitiveElement(j, k) + weight * grad[j] * grad[k]);
                    }
                    b.setElement(j, b.getPrimitiveElement(j) + weight * residual * grad[j]);
                }

            }
        }

        // solve the linearized least squares problem
        AbstractDoubleVector dX = LinearMathUtils.solve(a, b);

        // update the estimated parameters
        for (int i = 0; i < parameters.length; ++i) {
            parameters[i].setEstimate(parameters[i].getEstimate()
                    + dX.getPrimitiveElement(i));
        }

    }

    private double evaluateCriterion(EstimationProblem problem) {
        double criterion = 0.0;
        WeightedMeasurement[] measurements = problem.getMeasurements();

        for (int i = 0; i < measurements.length; ++i) {
            double residual = measurements[i].getResidual();
            criterion += measurements[i].getWeight() * residual * residual;
        }

        return criterion;

    }

    /**
     * Get the Root Mean Square value.
     * Get the Root Mean Square value, i.e. the root of the arithmetic
     * mean of the square of all residuals. This is related to the
     * criterion that is minimized by the estimator as follows: if
     * <em>c</em> if the criterion, and <em>n</em> is the number of
     * measurements, the the RMS is <em>sqrt (c/n)</em>.
     *
     * @param problem estimation problem
     * @return RMS value
     */
    public double getRMS(EstimationProblem problem) {
        double criterion = evaluateCriterion(problem);
        int n = problem.getMeasurements().length;
        return Math.sqrt(criterion / n);
    }

    private int maxIterations;
    private double steadyStateThreshold;
    private double convergence;
    private double epsilon;

}
