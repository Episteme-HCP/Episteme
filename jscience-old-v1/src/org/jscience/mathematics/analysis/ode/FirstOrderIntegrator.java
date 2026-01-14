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
 * This interface represents a first order integrator for
 * differential equations.
 * <p/>
 * <p>The classes which are devoted to solve first order differential
 * equations should implement this interface. The problems which can
 * be handled should implement the {@link
 * FirstOrderDifferentialEquations} interface.</p>
 *
 * @author L. Maisonobe
 * @version $Id: FirstOrderIntegrator.java,v 1.3 2007-10-23 18:19:19 virtualcall Exp $
 * @see FirstOrderDifferentialEquations
 * @see StepHandler
 * @see SwitchingFunction
 */
public interface FirstOrderIntegrator {
    /**
     * Get the name of the method.
     *
     * @return name of the method
     */
    public String getName();

    /**
     * Set the step handler for this integrator. The handler will be
     * called by the integrator for each accepted step.
     *
     * @param handler handler for the accepted steps
     */
    public void setStepHandler(StepHandler handler);

    /**
     * Get the step handler for this integrator.
     *
     * @return the step handler for this integrator
     */
    public StepHandler getStepHandler();

    /**
     * Add a switching function to the integrator.
     *
     * @param function switching function
     * @param maxCheckInterval maximal time interval between switching function
     *        checks (this interval prevents missing sign changes in case the
     *        integration steps becomes very large)
     * @param convergence convergence threshold in the event time search
     */
    public void addSwitchingFunction(SwitchingFunction function,
        double maxCheckInterval, double convergence);

    /**
     * Integrate the differential equations up to the given time
     *
     * @param equations differential equations to integrate
     * @param t0 initial time
     * @param y0 initial value of the state vector at t0
     * @param t target time for the integration (can be set to a value smaller
     *        thant <code>t0</code> for backward integration)
     * @param y placeholder where to put the state vector at each successful
     *        step (and hence at the end of integration), can be the same
     *        object as y0
     *
     * @throws DerivativeException this exception is propagated to the caller
     *         if the underlying user function triggers one
     * @throws IntegrationException if the integrator cannot perform
     *         integration
     */
    public void integrate(FirstOrderDifferentialEquations equations, double t0,
        double[] y0, double t, double[] y)
        throws DerivativeException, IntegrationException;
}
