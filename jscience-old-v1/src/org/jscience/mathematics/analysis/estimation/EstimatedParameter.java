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

package org.jscience.mathematics.analysis.estimation;

import org.jscience.util.Named;

import java.io.Serializable;

/**
 * This class represent the estimated parameters of an estimation problem.
 * <p/>
 * <p>The parameters of an estimation problem have a name, a value and
 * a bound flag. The value of bound parameters is considered trusted
 * and the solvers should not adjust them. On the other hand, the
 * solvers should adjust the value of unbounds parameters until they
 * satisfy convergence criterions specific to each solver.</p>
 *
 * @author L. Maisonobe
 * @version $Id: EstimatedParameter.java,v 1.2 2007-10-21 17:38:12 virtualcall Exp $
 */

public class EstimatedParameter
        implements Serializable, Named {

    /**
     * Simple constructor.
     * Build an instance from a first estimate of the parameter,
     * initially considered unbound.
     *
     * @param name          name of the parameter
     * @param firstEstimate first estimate of the parameter
     */
    public EstimatedParameter(String name, double firstEstimate) {
        this.name = name;
        estimate = firstEstimate;
        bound = false;
    }

    /**
     * Simple constructor.
     * Build an instance from a first estimate of the parameter and a
     * bound flag
     *
     * @param name          name of the parameter
     * @param firstEstimate first estimate of the parameter
     * @param bound         flag, should be true if the parameter is bound
     */
    public EstimatedParameter(String name,
                              double firstEstimate,
                              boolean bound) {
        this.name = name;
        estimate = firstEstimate;
        this.bound = bound;
    }

    /**
     * Copy constructor.
     * Build a copy of a parameter
     *
     * @param parameter instance to copy
     */
    public EstimatedParameter(EstimatedParameter parameter) {
        name = parameter.name;
        estimate = parameter.estimate;
        bound = parameter.bound;
    }

    /**
     * Set a new estimated value for the parameter.
     *
     * @param estimate new estimate for the parameter
     */
    public void setEstimate(double estimate) {
        this.estimate = estimate;
    }

    /**
     * Get the current estimate of the parameter
     *
     * @return current estimate
     */
    public double getEstimate() {
        return estimate;
    }

    /**
     * get the name of the parameter
     *
     * @return parameter name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the bound flag of the parameter
     *
     * @param bound this flag should be set to true if the parameter is
     *              bound (i.e. if it should not be adjusted by the solver).
     */
    public void setBound(boolean bound) {
        this.bound = bound;
    }

    /**
     * Check if the parameter is bound
     *
     * @return true if the parameter is bound
     */
    public boolean isBound() {
        return bound;
    }

    /**
     * Name of the parameter
     */
    private String name;

    /**
     * Current value of the parameter
     */
    protected double estimate;

    /**
     * Indicator for bound parameters
     * (ie parameters that should not be estimated)
     */
    private boolean bound;

}
