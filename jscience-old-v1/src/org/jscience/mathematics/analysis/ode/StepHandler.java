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
 * This interface represents a handler that should be called after
 * each successful step.
 * <p/>
 * <p>The ODE integrators compute the evolution of the state vector at
 * some grid points that depend on their own internal algorithm. Once
 * they have found a new grid point (possibly after having computed
 * several evaluation of the derivative at intermediate points), they
 * provide it to objects implementing this interface. These objects
 * typically either ignore the intermediate steps and wait for the
 * last one, store the points in an ephemeris, or forward them to
 * specialized processing or output methods.</p>
 *
 * @author L. Maisonobe
 * @version $Id: StepHandler.java,v 1.3 2007-10-23 18:19:19 virtualcall Exp $
 * @see FirstOrderIntegrator
 * @see SecondOrderIntegrator
 * @see StepInterpolator
 */
public interface StepHandler {
    /**
     * Determines whether this handler needs dense output.<p>This
     * method allows the integrator to avoid performing extra computation if
     * the handler does not need dense output. If this method returns false,
     * the integrator will call the {@link #handleStep} method with a {@link
     * DummyStepInterpolator} rather than a custom interpolator.</p>
     *
     * @return true if the handler needs dense output
     */
    public boolean requiresDenseOutput();

    /**
     * Reset the step handler. Initialize the internal data as required
     * before the first step is handled.
     */
    public void reset();

    /**
     * Handle the last accepted step
     *
     * @param interpolator interpolator for the last accepted step. For
     *        efficiency purposes, the various integrators reuse the same
     *        object on each call, so if the instance wants to keep it across
     *        all calls (for example to provide at the end of the integration
     *        a continuous model valid throughout the integration range, as
     *        the {@link ContinuousOutputModel ContinuousOutputModel} class
     *        does), it should build a local copy using the clone method of
     *        the interpolator and store this copy. Keeping only a reference
     *        to the interpolator and reusing it will result in unpredictable
     *        behaviour (potentially crashing the application).
     * @param isLast true if the step is the last one
     *
     * @throws DerivativeException this exception is propagated to the caller
     *         if the underlying user function triggers one
     */
    public void handleStep(StepInterpolator interpolator, boolean isLast)
        throws DerivativeException;
}
