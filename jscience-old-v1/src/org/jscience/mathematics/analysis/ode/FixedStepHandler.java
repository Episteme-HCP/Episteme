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
 * each successful fixed step.
 * <p/>
 * <p>This interface should be implemented by anyone who is interested
 * in getting the solution of an ordinary differential equation at
 * fixed time steps. Objects implementing this interface should be
 * wrapped within an instance of {@link StepNormalizer} that itself
 * is used as the general {@link StepHandler} by the integrator. The
 * {@link StepNormalizer} object is called according to the integrator
 * internal algorithms and it calls objects implementing this
 * interface as necessary at fixed time steps.</p>
 *
 * @author L. Maisonobe
 * @version $Id: FixedStepHandler.java,v 1.3 2007-10-23 18:19:19 virtualcall Exp $
 * @see StepHandler
 * @see StepNormalizer
 */
public interface FixedStepHandler {
    /**
     * Handle the last accepted step
     *
     * @param t time of the current step
     * @param y state vector at t. For efficiency purposes, the {@link
     *        StepNormalizer} class reuse the same array on each call, so if
     *        the instance wants to keep it across all calls (for example to
     *        provide at the end of the integration a complete array of all
     *        steps), it should build a local copy store this copy.
     * @param isLast true if the step is the last one
     */
    public void handleStep(double t, double[] y, boolean isLast);
}
