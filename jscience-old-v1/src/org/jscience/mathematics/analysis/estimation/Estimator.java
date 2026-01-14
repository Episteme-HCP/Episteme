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

/**
 * This interface represents solvers for estimation problems.
 * <p/>
 * <p>The classes which are devoted to solve estimation problems
 * should implement this interface. The problems which can be handled
 * should implement the {@link EstimationProblem} interface which
 * gather all the information needed by the solver.</p>
 * <p/>
 * <p>The interface is composed only of the {@link #estimate estimate}
 * method.</p>
 *
 * @author L. Maisonobe
 * @version $Id: Estimator.java,v 1.2 2007-10-21 17:38:12 virtualcall Exp $
 * @see EstimationProblem
 */

public interface Estimator {

    /**
     * Solve an estimation problem.
     * <p/>
     * <p>The method should set the parameters of the problem to several
     * trial values until it reaches convergence. If this method returns
     * normally (i.e. without throwing an exception), then the best
     * estimate of the parameters can be retrieved from the problem
     * itself, through the {@link EstimationProblem#getAllParameters
     * EstimationProblem.getAllParameters} method.</p>
     *
     * @param problem estimation problem to solve
     * @throws EstimationException if the problem cannot be solved
     */
    public void estimate(EstimationProblem problem)
            throws EstimationException;

}
