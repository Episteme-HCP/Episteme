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

import org.jscience.mathematics.analysis.MappingException;
import org.jscience.mathematics.analysis.PrimitiveMapping;


/**
 * This interface specifies root-finding methods for scalar functions.
 *
 * @author L. Maisonobe
 * @version $Id: RootsFinder.java,v 1.3 2007-10-23 18:19:26 virtualcall Exp $
 */
public interface RootsFinder {
    /**
     * Solve a function in a given interval known to contain a root.
     *
     * @param function function for which a root should be found
     * @param checker checker for the convergence of the function
     * @param maxIter maximal number of iteration allowed
     * @param x0 abscissa of the lower bound of the interval
     * @param f0 value of the function the lower bound of the interval
     * @param x1 abscissa of the higher bound of the interval
     * @param f1 value of the function the higher bound of the interval
     *
     * @return true if a root has been found in the given interval
     *
     * @throws MappingException DOCUMENT ME!
     */
    public boolean findRoot(PrimitiveMapping function,
        ConvergenceChecker checker, int maxIter, double x0, double f0,
        double x1, double f1) throws MappingException;

    /**
     * Get the abscissa of the root.
     *
     * @return abscissa of the root
     */
    public double getRoot();
}
