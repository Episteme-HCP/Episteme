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
 * This class is a step handler that do nothing.
 * <p/>
 * <p>This class is provided as a convenience for users who are only
 * interested in the final state of an integration and not in the
 * intermediate steps. Its handleStep method does nothing.</p>
 * <p/>
 * <p>Since this class has no internal state, it is implemented using
 * the Singleton design pattern. This means that only one instance is
 * ever created, which can be retrieved using the getInstance
 * method. This explains why there is no public constructor.</p>
 *
 * @author L. Maisonobe
 * @version $Id: DummyStepHandler.java,v 1.2 2007-10-21 17:38:17 virtualcall Exp $
 * @see StepHandler
 */

public class DummyStepHandler
        implements StepHandler {

    /**
     * Private constructor.
     * The constructor is private to prevent users from creating
     * instances (Singleton design-pattern).
     */
    private DummyStepHandler() {
    }

    /**
     * Get the only instance.
     *
     * @return the only instance
     */
    public static DummyStepHandler getInstance() {
        if (instance == null) {
            instance = new DummyStepHandler();
        }
        return instance;
    }

    /**
     * Determines whether this handler needs dense output.
     * Since this handler does nothing, it does not require dense output.
     *
     * @return always false
     */
    public boolean requiresDenseOutput() {
        return false;
    }

    /**
     * Reset the step handler.
     * Initialize the internal data as required before the first step is
     * handled.
     */
    public void reset() {
    }

    /**
     * Handle the last accepted step.
     * This method does nothing in this class.
     *
     * @param interpolator interpolator for the last accepted step. For
     *                     efficiency purposes, the various integrators reuse the same
     *                     object on each call, so if the instance wants to keep it across
     *                     all calls (for example to provide at the end of the integration a
     *                     continuous model valid throughout the integration range), it
     *                     should build a local copy using the clone method and store this
     *                     copy.
     * @param isLast       true if the step is the last one
     */
    public void handleStep(StepInterpolator interpolator, boolean isLast) {
    }

    /**
     * The only instance.
     */
    private static DummyStepHandler instance = null;

}
