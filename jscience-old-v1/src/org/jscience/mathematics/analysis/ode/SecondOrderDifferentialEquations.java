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
 * This interface represents a second order differential equations set.
 * <p/>
 * <p>This interface should be implemented by all real second order
 * differential equation problems before they can be handled by the
 * integrators {@link SecondOrderIntegrator#integrate} method.</p>
 * <p/>
 * <p>A second order differential equations problem, as seen by an
 * integrator is the second time derivative <code>d2Y/dt^2</code> of a
 * state vector <code>Y</code>, both being one dimensional
 * arrays. From the integrator point of view, this derivative depends
 * only on the current time <code>t</code>, on the state vector
 * <code>Y</code> and on the first time derivative of the state
 * vector.</p>
 * <p/>
 * <p>For real problems, the derivative depends also on parameters
 * that do not belong to the state vector (dynamical model constants
 * for example). These constants are completely outside of the scope
 * of this interface, the classes that implement it are allowed to
 * handle them as they want.</p>
 *
 * @author L. Maisonobe
 * @version $Id: SecondOrderDifferentialEquations.java,v 1.3 2007-10-23 18:19:19 virtualcall Exp $
 * @see SecondOrderIntegrator
 * @see FirstOrderConverter
 * @see FirstOrderDifferentialEquations
 * @see org.jscience.util.mapper.ArraySliceMappable
 */
public interface SecondOrderDifferentialEquations {
    /**
     * Get the dimension of the problem.
     *
     * @return dimension of the problem
     */
    public int getDimension();

    /**
     * Get the current time derivative of the state vector.
     *
     * @param t current value of the independant <I>time</I> variable
     * @param y array containing the current value of the state vector
     * @param yDot array containing the current value of the first derivative
     *        of the state vector
     * @param yDDot placeholder array where to put the second time derivative
     *        of the state vector
     *
     * @throws DerivativeException this exception is propagated to the caller
     *         if the underlying user function triggers one
     */
    public void computeSecondDerivatives(double t, double[] y, double[] yDot,
        double[] yDDot) throws DerivativeException;
}
