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

package org.jscience.mathematics.analysis.ode;

/**
 * This class implements a linear interpolator for step.
 * <p/>
 * <p>This interpolator allow to compute dense output inside the last
 * step computed. The interpolation equation is consistent with the
 * integration scheme :
 * <p/>
 * <pre>
 *   y(t_n + theta h) = y (t_n + h) - (1-theta) h y'
 * </pre>
 * <p/>
 * where theta belongs to [0 ; 1] and where y' is the evaluation of
 * the derivatives already computed during the step.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EulerStepInterpolator.java,v 1.2 2007-10-21 17:38:17 virtualcall Exp $
 * @see EulerIntegrator
 */

class EulerStepInterpolator
        extends RungeKuttaStepInterpolator {

    /**
     * Simple constructor.
     * This constructor builds an instance that is not usable yet, the
     * {@link #reinitialize} method should be called before using the
     * instance in order to initialize the internal arrays. This
     * constructor is used only in order to delay the initialization in
     * some cases. The {@link RungeKuttaIntegrator} class uses the
     * prototyping design pattern to create the step interpolators by
     * cloning an uninitialized model and latter initializing the copy.
     */
    public EulerStepInterpolator() {
    }

    /**
     * Copy constructor.
     *
     * @param interpolator interpolator to copy from. The copy is a deep
     *                     copy: its arrays are separated from the original arrays of the
     *                     instance
     */
    public EulerStepInterpolator(EulerStepInterpolator interpolator) {
        super(interpolator);
    }

    /**
     * Clone the instance.
     * the copy is a deep copy: its arrays are separated from the
     * original arrays of the instance
     *
     * @return a copy of the instance
     */
    public Object clone() {
        return new EulerStepInterpolator(this);
    }

    /**
     * Compute the state at the interpolated time.
     * This is the main processing method that should be implemented by
     * the derived classes to perform the interpolation.
     *
     * @param theta          normalized interpolation abscissa within the step
     *                       (theta is zero at the previous time step and one at the current time step)
     * @param oneMinusThetaH time gap between the interpolated time and
     *                       the current time
     * @throws DerivativeException this exception is propagated to the caller if the
     *                             underlying user function triggers one
     */
    protected void computeInterpolatedState(double theta,
                                            double oneMinusThetaH)
            throws DerivativeException {

        for (int i = 0; i < interpolatedState.length; ++i) {
            interpolatedState[i] = currentState[i] - oneMinusThetaH * yDotK[0][i];
        }

    }

}
