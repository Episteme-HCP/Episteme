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

package org.jscience.computing.ai.planning;

import java.util.Comparator;


/**
 * This class handles <code>:sort-by</code> logical preconditions that use
 * numerical <b>more than</b> as the sorting function.
 *
 * @author Okhtay Ilghami
 * @author <a
 *         href="http://www.cs.umd.edu/~okhtay">http://www.cs.umd.edu/~okhtay</a>
 * @version 1.0.2
 */
public class CompMore implements Comparator {
    /**
     * The index of the variable according to the value of which the
     * satisfiers should be sorted.
     */
    private int varIdx;

/**
     * To initialize this object.
     *
     * @param varIdxIn The index of the variable according to the value of
     *                 which the satisfiers should be sorted.
     */
    public CompMore(int varIdxIn) {
        varIdx = varIdxIn;
    }

    /**
     * The function that implements the actual comparison.
     *
     * @param o1 the first binding.
     * @param o2 the second binding.
     *
     * @return -1 if the first binding should come first, 1 if the second
     *         binding should come first, 0 if the variable according to value
     *         of which the satisfiers are being sorted has the same value in
     *         these two bindings.
     */
    public int compare(Object o1, Object o2) {
        Term[] t1 = (Term[]) o1;
        Term[] t2 = (Term[]) o2;

        //-- Get the numerical values of the two terms.
        double n1 = ((TermNumber) t1[varIdx]).getNumber();
        double n2 = ((TermNumber) t2[varIdx]).getNumber();

        //-- Compare them and return the result.
        if (n1 > n2) {
            return -1;
        }

        if (n1 < n2) {
            return 1;
        }

        return 0;
    }
}
