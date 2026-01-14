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

import java.io.Externalizable;


/**
 * This interface represents an interpolator over the last step
 * during an ODE integration.
 * <p/>
 * <p>The various ODE integrators provide objects implementing this
 * interface to the step handlers. These objects are often custom
 * objects tightly bound to the integrator internal algorithms. The
 * handlers can use these objects to retrieve the state vector at
 * intermediate times between the previous and the current grid points
 * (this feature is often called dense output).</p>
 *
 * @author L. Maisonobe
 * @version $Id: StepInterpolator.java,v 1.3 2007-10-23 18:19:19 virtualcall Exp $
 * @see FirstOrderIntegrator
 * @see SecondOrderIntegrator
 * @see StepHandler
 */
public interface StepInterpolator extends Externalizable {
    /**
     * Get the previous grid point time.
     *
     * @return previous grid point time
     */
    public double getPreviousTime();

    /**
     * Get the current grid point time.
     *
     * @return current grid point time
     */
    public double getCurrentTime();

    /**
     * Get the time of the interpolated point. If {@link
     * #setInterpolatedTime} has not been called, it returns the current grid
     * point time.
     *
     * @return interpolation point time
     */
    public double getInterpolatedTime();

    /**
     * Set the time of the interpolated point.<p>Setting the time
     * outside of the current step is now allowed (it was not allowed up to
     * version 5.4 of Mantissa), but should be used with care since the
     * accuracy of the interpolator will probably be very poor far from this
     * step. This allowance has been added to simplify implementation of
     * search algorithms near the step endpoints.</p>
     *
     * @param time time of the interpolated point
     *
     * @throws DerivativeException if this call induces an automatic step
     *         finalization that throws one
     */
    public void setInterpolatedTime(double time) throws DerivativeException;

    /**
     * Get the state vector of the interpolated point.
     *
     * @return state vector at time {@link #getInterpolatedTime}
     */
    public double[] getInterpolatedState();

    /**
     * Check if the natural integration direction is forward.<p>This
     * method provides the integration direction as specified by the
     * integrator itself, it avoid some nasty problems in degenerated cases
     * like null steps due to cancellation at step initialization, step
     * control or switching function triggering.</p>
     *
     * @return true if the integration variable (time) increases during
     *         integration
     */
    public boolean isForward();
}
