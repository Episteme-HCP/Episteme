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
public class Unifier {
    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean unify(WFF a, WFF b) {
        b.increaseVariables(a.getMaxVariable());

        int wffA = WFF.A;

        for (int i = 0; i < a.getLength(); i++) {
            if (i >= b.getLength()) {
                return false;
            }

            byte ach = a.getToken(i);
            byte bch = b.getToken(i);

            if (ach != bch) {
                if (bch >= wffA) {
                    WFF sub = a.getSubWFF(i);

                    if (sub.contains(bch) || !a.substitute(bch, sub) ||
                            !b.substitute(bch, sub)) {
                        return false;
                    }
                } else if (ach >= wffA) {
                    WFF sub = b.getSubWFF(i);

                    if (sub.contains(ach) || !a.substitute(ach, sub) ||
                            !b.substitute(ach, sub)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        a.normalize();
        b.normalize();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        //		WFF a = new WFF(":>>a>~b~c>a>cb");
        //		WFF b = new WFF(":>~a>ab");
        WFF a = new WFF(":>a>~b~c");
        WFF b = new WFF(":>~a>ab");
        System.out.println(Unifier.unify(a, b));
        System.out.println("a" + a);
        System.out.println("b" + b);
    }
}
