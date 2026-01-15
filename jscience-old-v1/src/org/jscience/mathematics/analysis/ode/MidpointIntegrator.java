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
