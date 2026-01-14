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

import JSHOP2.*;

import java.util.Comparator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class MyComparator implements Comparator {
    /**
     * DOCUMENT ME!
     */
    private int varIdx;

    /**
     * Creates a new MyComparator object.
     *
     * @param varIdxIn DOCUMENT ME!
     */
    public MyComparator(int varIdxIn) {
        varIdx = varIdxIn;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Object o1, Object o2) {
        Term[] t1 = (Term[]) o1;
        Term[] t2 = (Term[]) o2;

        //-- Get the numerical values of the two terms.
        int n1 = (int) ((TermNumber) t1[varIdx]).getNumber();
        int n2 = (int) ((TermNumber) t2[varIdx]).getNumber();

        //-- Compare them and return the result. This particular comparison
        //-- function prefers odd numbers to even numbers.
        if ((n1 & 1) == 1) {
            return -1;
        }
        else if ((n2 & 1) == 1) {
            return 1;
        }

        return 0;
    }
}
