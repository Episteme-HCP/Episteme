/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.analysis.fitting;

import org.jscience.mathematics.analysis.estimation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is the base class for all curve fitting classes in the package.
 * <p/>
 * <p>This class handles all common features of curve fitting like the
 * sample points handling. It declares two methods ({@link
 * #valueAt} and {@link #partial}) which should be implemented by
 * sub-classes to define the precise shape of the curve they
 * represent.</p>
 *
 * @author L. Maisonobe
 * @version $Id: AbstractCurveFitter.java,v 1.2 2007-10-21 17:38:16 virtualcall Exp $
 */

public abstract class AbstractCurveFitter
        implements EstimationProblem, Serializable {

    /**
     * Simple constructor.
     *
     * @param n                    number of coefficients in the underlying function
     * @param maxIterations        maximum number of iterations allowed
     * @param convergence          criterion threshold below which we do not need
     *                             to improve the criterion anymore
     * @param steadyStateThreshold steady state detection threshold, the
     *                             problem has reached a steady state (read converged) if
     *                             <code>Math.abs (Jn - Jn-1) < Jn * convergence</code>, where
     *                             <code>Jn</code> and <code>Jn-1</code> are the current and
     *                             preceding criterion value (square sum of the weighted residuals
     *                             of considered measurements).
     * @param epsilon              threshold under which the matrix of the linearized
     *                             problem is considered singular (see {@link
     *                             org.jscience.mathematics.analysis.linalg.SquareMatrix#solve
     *                             SquareMatrix.solve}).
     */
    protected AbstractCurveFitter(int n,
                                  int maxIterations,
                                  double convergence,
                                  double steadyStateThreshold,
                                  double epsilon) {

        coefficients = new EstimatedParameter[n];
        measurements = new ArrayList();
        measurementsArray = null;
        this.maxIterations = maxIterations;
        this.steadyStateThreshold = steadyStateThreshold;
        this.convergence = convergence;
        this.epsilon = epsilon;

    }

    /**
     * Simple constructor.
     *
     * @param coefficients         first estimate of the coefficients. A
     *                             reference to this array is hold by the newly created object. Its
     *                             elements will be adjusted during the fitting process and they will
     *                             be set to the adjusted coefficients at the end.
     * @param maxIterations        maximum number of iterations allowed
     * @param convergence          criterion threshold below which we do not need
     *                             to improve the criterion anymore
     * @param steadyStateThreshold steady state detection threshold, the
     *                             problem has reached a steady state (read converged) if
     *                             <code>Math.abs (Jn - Jn-1) < Jn * convergence</code>, where
     *                             <code>Jn</code> and <code>Jn-1</code> are the current and
     *                             preceding criterion value (square sum of the weighted residuals
     *                             of considered measurements).
     * @param epsilon              threshold under which the matrix of the linearized
     *                             problem is considered singular (see {@link
     *                             org.jscience.mathematics.analysis.linalg.SquareMatrix#solve
     *                             SquareMatrix.solve}).
     */
    protected AbstractCurveFitter(EstimatedParameter[] coefficients,
                                  int maxIterations,
                                  double convergence,
                                  double steadyStateThreshold,
                                  double epsilon) {

        this.coefficients = coefficients;
        measurements = new ArrayList();
        measurementsArray = null;
        this.maxIterations = maxIterations;
        this.steadyStateThreshold = steadyStateThreshold;
        this.convergence = convergence;
        this.epsilon = epsilon;
    }

    /**
     * Add a weighted (x,y) pair to the sample.
     *
     * @param weight weight of this pair in the fit
     * @param x      abscissa
     * @param y      ordinate, we have <code>y = f (x)</code>
     */
    public void addWeightedPair(double weight, double x, double y) {
        measurementsArray = null;
        measurements.add(new FitMeasurement(weight, x, y));
    }

    /**
     * Perform the fitting.
     * <p/>
     * <p>This method compute the coefficients of the curve that best
     * fit the sample of weighted pairs previously given through calls
     * to the {@link #addWeightedPair addWeightedPair} method.</p>
     *
     * @return coefficients of the curve
     * @throws EstimationException if the fitting is not possible
     *                             (for example if the sample has to few independant points)
     */
    public double[] fit()
            throws EstimationException {
        // perform the fit using a linear least square estimator
        new LeastSquaresEstimator(maxIterations, convergence,
                steadyStateThreshold, epsilon).estimate(this);

        // extract the coefficients
        double[] fittedCoefficients = new double[coefficients.length];
        for (int i = 0; i < coefficients.length; ++i) {
            fittedCoefficients[i] = coefficients[i].getEstimate();
        }

        return fittedCoefficients;

    }

    public WeightedMeasurement[] getMeasurements() {
        if (measurementsArray == null) {
            measurementsArray = new FitMeasurement[measurements.size()];
            int i = 0;
            for (Iterator iterator = measurements.iterator(); iterator.hasNext(); ++i) {
                measurementsArray[i] = (FitMeasurement) iterator.next();
            }
        }
        return measurementsArray;
    }

    /**
     * Get the unbound parameters of the problem.
     * For a curve fitting, none of the function coefficient is bound.
     *
     * @return unbound parameters
     */
    public EstimatedParameter[] getUnboundParameters() {
        return coefficients;
    }

    /**
     * Get all the parameters of the problem.
     *
     * @return parameters
     */
    public EstimatedParameter[] getAllParameters() {
        return coefficients;
    }

    /**
     * Utility method to sort the measurements with respect to the abscissa.
     * <p/>
     * <p>This method is provided as a utility for derived classes. As
     * an example, the {@link HarmonicFitter} class needs it in order to
     * compute a first guess of the coefficients to initialize the
     * estimation algorithm.</p>
     */
    protected void sortMeasurements() {

        // Since the samples are almost always already sorted, this
        // method is implemented as an insertion sort that reorders the
        // elements in place. Insertion sort is very efficient in this case.
        FitMeasurement curr = (FitMeasurement) measurements.get(0);
        for (int j = 1; j < measurements.size(); ++j) {
            FitMeasurement prec = curr;
            curr = (FitMeasurement) measurements.get(j);
            if (curr.x < prec.x) {
                // the current element should be inserted closer to the beginning
                int i = j - 1;
                FitMeasurement mI = (FitMeasurement) measurements.get(i);
                while ((i >= 0) && (curr.x < mI.x)) {
                    measurements.set(i + 1, mI);
                    if (i-- != 0) {
                        mI = (FitMeasurement) measurements.get(i);
                    } else {
                        mI = null;
                    }
                }
                measurements.set(i + 1, curr);
                curr = (FitMeasurement) measurements.get(j);
            }
        }

        // make sure subsequent calls to getMeasurements
        // will not use the unsorted array
        measurementsArray = null;

    }

    /**
     * Get the value of the function at x according to the current parameters value.
     *
     * @param x abscissa at which the theoretical value is requested
     * @return theoretical value at x
     */
    public abstract double valueAt(double x);

    /**
     * Get the derivative of the function at x with respect to parameter p.
     *
     * @param x abscissa at which the partial derivative is requested
     * @param p parameter with respect to which the derivative is requested
     * @return partial derivative
     */
    public abstract double partial(double x, EstimatedParameter p);

    /**
     * This class represents the fit measurements.
     * One measurement is a weighted pair (x, y), where <code>y = f
     * (x)</code> is the value of the function at x abscissa. This class
     * is an inner class because the methods related to the computation
     * of f values and derivative are proveded by the fitter
     * implementations.
     */
    public class FitMeasurement
            extends WeightedMeasurement {

        /**
         * Simple constructor.
         *
         * @param weight weight of the measurement in the fitting process
         * @param x      abscissa of the measurement
         * @param y      ordinate of the measurement
         */
        public FitMeasurement(double weight, double x, double y) {
            super(weight, y);
            this.x = x;
        }

        /**
         * Get the value of the fitted function at x.
         *
         * @return theoretical value at the measurement abscissa
         */
        public double getTheoreticalValue() {
            return valueAt(x);
        }

        /**
         * Partial derivative with respect to one function coefficient.
         *
         * @param p parameter with respect to which the derivative is requested
         * @return partial derivative
         */
        public double getPartial(EstimatedParameter p) {
            return partial(x, p);
        }

        /**
         * Abscissa of the measurement.
         */
        public final double x;

    }

    /**
     * Coefficients of the function
     */
    protected EstimatedParameter[] coefficients;

    /**
     * Measurements vector
     */
    protected List measurements;

    /**
     * Measurements array.
     * This array contains the same entries as measurements_, but in a
     * different structure.
     */
    private FitMeasurement[] measurementsArray;

    private int maxIterations;
    private double convergence;
    private double steadyStateThreshold;
    private double epsilon;

}
