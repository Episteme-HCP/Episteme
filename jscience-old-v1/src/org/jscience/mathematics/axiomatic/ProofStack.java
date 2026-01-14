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

package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public final class ProofStack {
    /** DOCUMENT ME! */
    public static int DEPTH = 4;

    /** DOCUMENT ME! */
    private WFF[] proofStack;

    /** DOCUMENT ME! */
    private int stackLength = 0;

/**
     * Creates a new ProofStack object.
     *
     * @param capacity DOCUMENT ME!
     */
    public ProofStack(int capacity) {
        this.proofStack = new WFF[capacity];
    }

    /**
     * DOCUMENT ME!
     *
     * @param wff DOCUMENT ME!
     */
    public void push(String wff) {
        this.push(new WFF(wff));
    }

    /**
     * DOCUMENT ME!
     *
     * @param wff DOCUMENT ME!
     */
    public void push(WFF wff) {
        this.proofStack[this.stackLength++] = wff;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WFF pop() {
        return this.proofStack[--this.stackLength];
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        this.stackLength = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return this.stackLength == 0;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        this.stackLength = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WFF deduce() {
        int lstackLength = this.stackLength;

        if (lstackLength < 2) {
            System.out.println("Short stack");

            return null;
        }

        WFF ab = this.pop();
        WFF[] split = ab.split();

        if (split == null) {
            return null;
        }

        WFF a = split[0];
        WFF b = split[1];
        WFF c = this.pop();
        c.increaseVariables(ab.getMaxVariable());

        //        System.out.println("A" + a);
        //      System.out.println("B" + b);
        //    System.out.println("C" + c);
        int wffA = WFF.A;

        for (int i = 0; i < c.getLength(); i++) {
            if (i >= a.getLength()) {
                return null;
            }

            byte ach = a.getToken(i);
            byte csubch = c.getToken(i);

            if (ach != csubch) {
                if (csubch >= wffA) {
                    WFF sub = a.getSubWFF(i);

                    if (sub.contains(csubch) || !a.substitute(csubch, sub) ||
                            !b.substitute(csubch, sub) ||
                            !c.substitute(csubch, sub)) {
                        return null;
                    }
                } else if (ach >= wffA) {
                    WFF sub = c.getSubWFF(i);

                    if (sub.contains(ach) || !a.substitute(ach, sub) ||
                            !b.substitute(ach, sub) || !c.substitute(ach, sub)) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }

        b.mergeHypotheses(c);
        b.normalize();

        if (b.contains(DEPTH + WFF.A)) {
            return null;
        }

        //        System.out.println("?" + b);
        this.push(b);

        return b;
    }
}
