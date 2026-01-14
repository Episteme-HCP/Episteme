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
 * This interface represents a switching function.
 * <p/>
 * <p>A switching function allows to handle discrete events in
 * integration problems. These events occur for example when the
 * integration process should be stopped as some value is reached
 * (G-stop facility), or when the derivatives has
 * discontinuities. These events are traditionally defined as
 * occurring when a <code>g</code> function sign changes.</p>
 * <p/>
 * <p>Since events are only problem-dependent and are triggered by the
 * independant <i>time</i> variable and the state vector, they can
 * occur at virtually any time. The integrators will take care to
 * avoid sign changes inside the steps, they will reduce the step size
 * when such an event is detected in order to put this event exactly
 * at the end of the current step. This guarantees that step
 * interpolation (which always has a one step scope) is relevant even
 * in presence of discontinuities. This is independent from the
 * stepsize control provided by integrators that monitor the local
 * error (this feature is available on all integrators, including
 * fixed step ones).</p>
 *
 * @author L. Maisonobe
 * @version $Id: SwitchingFunction.java,v 1.2 2007-10-21 17:38:17 virtualcall Exp $
 */

public interface SwitchingFunction {

    /**
     * Stop indicator.
     * <p>This value should be used as the return value of the {@link
     * #eventOccurred eventOccurred} method when the integration should be
     * stopped after the event ending the current step.</p>
     */
    public static final int STOP = 0;

    /**
     * Reset indicator.
     * <p>This value should be used as the return value of the {@link
     * #eventOccurred eventOccurred} method when the integration should
     * go on after the event ending the current step, with a new state
     * vector (which will be retrieved through the {@link #resetState
     * resetState} method).</p>
     */
    public static final int RESET = 1;

    /**
     * Continue indicator.
     * <p>This value should be used as the return value of the {@link
     * #eventOccurred eventOccurred} method when the integration should go
     * on after the event ending the current step.</p>
     */
    public static final int CONTINUE = 2;

    /**
     * Compute the value of the switching function.
     * <p/>
     * <p>Discrete events are generated when the sign of this function
     * changes, the integrator will take care to change the stepsize in
     * such a way these events occur exactly at step boundaries. This
     * function must be continuous, as the integrator will need to find
     * its roots to locate the events.</p>
     *
     * @param t current value of the independant <i>time</i> variable
     * @param y array containing the current value of the state vector
     * @return value of the g function
     */
    public double g(double t, double[] y);

    /**
     * Handle an event and choose what to do next.
     * <p/>
     * <p>This method is called when the integrator has accepted a step
     * ending exactly on a sign change of the function, just before the
     * step handler itself is called. It allows the user to update his
     * internal data to acknowledge the fact the event has been handled
     * (for example setting a flag to switch the derivatives computation
     * in case of discontinuity), and it allows to direct the integrator
     * to either stop or continue integration, possibly with a reset
     * state.</p>
     * <p/>
     * <p>If {@link #STOP} is returned, the step handler will be called
     * with the <code>isLast</code> flag of the {@link
     * StepHandler#handleStep handleStep} method set to true. If {@link
     * #RESET} is returned, the {@link #resetState resetState} method
     * will be called once the step handler has finished its task.</p>
     *
     * @param t current value of the independant <i>time</i> variable
     * @param y array containing the current value of the state vector
     * @return indication of what the integrator should do next, this
     *         value must be one of {@link #STOP}, {@link #RESET} or {@link
     *         #CONTINUE}
     */
    public int eventOccurred(double t, double[] y);

    /**
     * Reset the state prior to continue the integration.
     * <p/>
     * <p>This method is called after the step handler has returned and
     * before the next step is started, but only when {@link
     * #eventOccurred} has itself returned the {@link #RESET}
     * indicator. It allows the user to reset the state vector for the
     * next step, without perturbing the step handler of the finishing
     * step. If the {@link #eventOccurred} never returns the {@link
     * #RESET} indicator, this function will never be called, and it is
     * safe to leave its body empty.</p>
     *
     * @param t current value of the independant <i>time</i> variable
     * @param y array containing the current value of the state vector
     *          the new state should be put in the same array
     */
    public void resetState(double t, double[] y);

}
