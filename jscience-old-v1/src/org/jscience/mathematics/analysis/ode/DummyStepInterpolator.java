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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class is a step interpolator that does nothing.
 * <p/>
 * <p>This class is used when the {@link StepHandler "step handler"}
 * set up by the user does not need step interpolation. It does not
 * recompute the state when {@link #setInterpolatedTime
 * setInterpolatedTime} is called. This implies the interpolated state
 * is always the state at the end of the current step.</p>
 *
 * @author L. Maisonobe
 * @version $Id: DummyStepInterpolator.java,v 1.2 2007-10-21 17:38:17 virtualcall Exp $
 * @see StepHandler
 */

public class DummyStepInterpolator
        extends AbstractStepInterpolator {

    /**
     * Simple constructor.
     * This constructor builds an instance that is not usable yet, the
     * {@link #reinitialize} method should be called before using the
     * instance in order to initialize the internal arrays. This
     * constructor is used only in order to delay the initialization in
     * some cases. As an example, the {@link
     * RungeKuttaFehlbergIntegrator} uses the prototyping design pattern
     * to create the step interpolators by cloning an uninitialized
     * model and latter initializing the copy.
     */
    protected DummyStepInterpolator() {
        super();
    }

    /**
     * Simple constructor.
     *
     * @param y       reference to the integrator array holding the state at
     *                the end of the step
     * @param forward integration direction indicator
     */
    protected DummyStepInterpolator(double[] y, boolean forward) {
        super(y, forward);
    }

    /**
     * Copy constructor.
     * <p/>
     * <p>The copied interpolator should have been finalized before the
     * copy, otherwise the copy will not be able to perform correctly
     * any interpolation and will throw a {@link NullPointerException}
     * later. Since we don't want this constructor to throw the
     * exceptions finalization may involve and since we don't want this
     * method to modify the state of the copied interpolator,
     * finalization is <strong>not</strong> done automatically, it
     * remains under user control.</p>
     * <p/>
     * <p>The copy is a deep copy: its arrays are separated from the
     * original arrays of the instance.</p>
     *
     * @param interpolator interpolator to copy from.
     */
    protected DummyStepInterpolator(DummyStepInterpolator interpolator) {
        super(interpolator);
    }

    /**
     * Copy the instance.
     * the copy is a deep copy: its arrays are separated from the
     * original arrays of the instance
     *
     * @return a copy of the instance.
     */
    public Object clone() {
        return new DummyStepInterpolator(this);
    }

    /**
     * Compute the state at the interpolated time.
     * In this class, this method does nothing: the interpolated state
     * is always the state at the end of the current step.
     *
     * @param theta          normalized interpolation abscissa within the step
     *                       (theta is zero at the previous time step and one at the current time step)
     * @param oneMinusThetaH time gap between the interpolated time and
     *                       the current time
     * @throws DerivativeException this exception is propagated to the caller if the
     *                             underlying user function triggers one
     */
    protected void computeInterpolatedState(double theta, double oneMinusThetaH)
            throws DerivativeException {
    }

    public void writeExternal(ObjectOutput out)
            throws IOException {
        // save the state of the base class
        writeBaseExternal(out);
    }

    public void readExternal(ObjectInput in)
            throws IOException {

        // read the base class
        double t = readBaseExternal(in);

        try {
            // we can now set the interpolated time and state
            setInterpolatedTime(t);
        } catch (DerivativeException e) {
            IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        }

    }

}
