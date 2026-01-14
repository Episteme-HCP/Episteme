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
 * This class implements a second order Runge-Kutta integrator for
 * Ordinary Differential Equations.
 * <p/>
 * <p>This method is an explicit Runge-Kutta method, its Butcher-array
 * is the following one :
 * <pre>
 *    0  |  0    0
 *   1/2 | 1/2   0
 *       |----------
 *       |  0    1
 * </pre>
 * </p>
 *
 * @author L. Maisonobe
 * @version $Id: MidpointIntegrator.java,v 1.2 2007-10-21 17:38:17 virtualcall Exp $
 * @see EulerIntegrator
 * @see ClassicalRungeKuttaIntegrator
 * @see GillIntegrator
 */

public class MidpointIntegrator
        extends RungeKuttaIntegrator implements Named {

    private static final String methodName = new String("midpoint");

    private static final double[] c = {
            1.0 / 2.0
    };

    private static final double[][] a = {
            {1.0 / 2.0}
    };

    private static final double[] b = {
            0.0, 1.0
    };

    /**
     * Simple constructor.
     * Build a midpoint integrator with the given step.
     *
     * @param step integration step
     */
    public MidpointIntegrator(double step) {
        super(false, c, a, b, new MidpointStepInterpolator(), step);
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
