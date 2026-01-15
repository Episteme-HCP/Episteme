package org.jscience.mathematics.analysis.ode;

/**
 * This interface represents a first order differential equations set.
 * <p/>
 * <p>This interface should be implemented by all real first order
 * differential equation problems before they can be handled by the
 * integrators {@link FirstOrderIntegrator#integrate} method.</p>
 * <p/>
 * <p>A first order differential equations problem, as seen by an
 * integrator is the time derivative <code>dY/dt</code> of a state
 * vector <code>Y</code>, both being one dimensional arrays. From the
 * integrator point of view, this derivative depends only on the
 * current time <code>t</code> and on the state vector
 * <code>Y</code>.</p>
 * <p/>
 * <p>For real problems, the derivative depends also on parameters
 * that do not belong to the state vector (dynamical model constants
 * for example). These constants are completely outside of the scope
 * of this interface, the classes that implement it are allowed to
 * handle them as they want.</p>
 *
 * @author L. Maisonobe
 * @version $Id: FirstOrderDifferentialEquations.java,v 1.3 2007-10-23 18:19:18 virtualcall Exp $
 * @see FirstOrderIntegrator
 * @see FirstOrderConverter
 * @see SecondOrderDifferentialEquations
 * @see org.jscience.util.mapper.ArraySliceMappable
 */
public interface FirstOrderDifferentialEquations {
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
     * @param yDot placeholder array where to put the time derivative of the
     *        state vector
     *
     * @throws DerivativeException this exception is propagated to the caller
     *         if the underlying user function triggers one
     */
    public void computeDerivatives(double t, double[] y, double[] yDot)
        throws DerivativeException;
}
