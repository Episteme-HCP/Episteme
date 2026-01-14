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

package org.jscience.mathematics.analysis.estimation;

/**
 * This interface represents an estimation problem.
 * <p/>
 * <p>This interface should be implemented by all real estimation
 * problems before they can be handled by the estimators through the
 * {@link Estimator#estimate Estimator.estimate} method.</p>
 * <p/>
 * <p>An estimation problem, as seen by a solver is a set of
 * parameters and a set of measurements. The parameters are adjusted
 * during the estimation through the {@link #getUnboundParameters
 * getUnboundParameters} and {@link EstimatedParameter#setEstimate
 * EstimatedParameter.setEstimate} methods. The measurements both have
 * a measured value which is generally fixed at construction and a
 * theoretical value which depends on the model and hence varies as
 * the parameters are adjusted. The purpose of the solver is to reduce
 * the residual between these values, it can retrieve the measurements
 * through the {@link #getMeasurements getMeasurements} method.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EstimationProblem.java,v 1.3 2007-10-23 18:19:01 virtualcall Exp $
 * @see Estimator
 * @see WeightedMeasurement
 */
public interface EstimationProblem {
    /**
     * Get the measurements of an estimation problem.
     *
     * @return measurements
     */
    public WeightedMeasurement[] getMeasurements();

    /**
     * Get the unbound parameters of the problem.
     *
     * @return unbound parameters
     */
    public EstimatedParameter[] getUnboundParameters();

    /**
     * Get all the parameters of the problem.
     *
     * @return parameters
     */
    public EstimatedParameter[] getAllParameters();
}
