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

package org.jscience.mathematics.analysis.roots;

/**
 * This interface specifies methods to check if a root-finding
 * algorithm has converged.
 * <p/>
 * Deciding if convergence has been reached is a problem-dependent
 * issue. The user should provide a class implementing this interface
 * to allow the root-finding algorithm to stop its search according to
 * the problem at hand.
 *
 * @author L. Maisonobe
 * @version $Id: ConvergenceChecker.java,v 1.3 2007-10-23 18:19:26 virtualcall Exp $
 */
public interface ConvergenceChecker {
    /** Indicator for no convergence. */
    public static final int NONE = 0;

    /** Indicator for convergence on the lower bound of the interval. */
    public static final int LOW = 1;

    /** Indicator for convergence on the higher bound of the interval. */
    public static final int HIGH = 2;

    /**
     * Check if the root-finding algorithm has converged on the
     * interval. The interval defined by the arguments contains one root (if
     * there was at least one in the initial interval given by the user to the
     * root-finding algorithm, of course)
     *
     * @param xLow abscissa of the lower bound of the interval
     * @param fLow value of the function the lower bound of the interval
     * @param xHigh abscissa of the higher bound of the interval
     * @param fHigh value of the function the higher bound of the interval
     *
     * @return convergence indicator, must be one of {@link #NONE}, {@link
     *         #LOW} or {@link #HIGH}
     */
    public int converged(double xLow, double fLow, double xHigh, double fHigh);
}
