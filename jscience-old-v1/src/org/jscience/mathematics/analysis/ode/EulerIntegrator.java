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

import org.jscience.util.Named;

/**
 * This class implements a simple Euler integrator for Ordinary
 * Differential Equations.
 * <p/>
 * <p>The Euler algorithm is the simplest one that can be used to
 * integrate ordinary differential equations. It is a simple inversion
 * of the forward difference expression :
 * <code>f'=(f(t+h)-f(t))/h</code> which leads to
 * <code>f(t+h)=f(t)+hf'</code>. The interpolation scheme used for
 * dense output is the linear scheme already used for integration.</p>
 * <p/>
 * <p>This algorithm looks cheap because it needs only one function
 * evaluation per step. However, as it uses linear estimates, it needs
 * very small steps to achieve high accuracy, and small steps lead to
 * numerical errors and instabilities.</p>
 * <p/>
 * <p>This algorithm is almost never used and has been included in
 * this package only as a comparison reference for more useful
 * integrators.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EulerIntegrator.java,v 1.2 2007-10-21 17:38:17 virtualcall Exp $
 * @see MidpointIntegrator
 * @see ClassicalRungeKuttaIntegrator
 * @see GillIntegrator
 * @see ThreeEighthesIntegrator
 */

public class EulerIntegrator
        extends RungeKuttaIntegrator implements Named {

    private static final String methodName = new String("Euler");

    private static final double[] c = {
    };

    private static final double[][] a = {
    };

    private static final double[] b = {
            1.0
    };

    /**
     * Simple constructor.
     * Build an Euler integrator with the given step.
     *
     * @param step integration step
     */
    public EulerIntegrator(double step) {
        super(false, c, a, b, new EulerStepInterpolator(), step);
    }

    /**
     * Get the name of the method.
     *
     * @return name of the method
     */
    public String getName() {
        return methodName;
    }

}
