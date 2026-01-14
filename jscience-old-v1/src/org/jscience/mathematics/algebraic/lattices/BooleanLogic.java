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

package org.jscience.mathematics.algebraic.lattices;

import org.jscience.mathematics.algebraic.numbers.Boolean;


/**
 * The BooleanLattice class encapsulates the tradionnal boolean algebra.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class BooleanLogic extends Object implements BooleanAlgebra {
    /** DOCUMENT ME! */
    public final static Boolean FALSE = new Boolean(false);

    /** DOCUMENT ME! */
    public final static Boolean TRUE = new Boolean(true);

    /** DOCUMENT ME! */
    private static BooleanLogic _instance;

/**
     * Constructs a boolean logic.
     */
    private BooleanLogic() {
    }

    /**
     * Constructs a algebra of boolean logic numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final BooleanLogic getInstance() {
        if (_instance == null) {
            synchronized (BooleanLogic.class) {
                if (_instance == null) {
                    _instance = new BooleanLogic();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the boolean number zero.
     *
     * @return DOCUMENT ME!
     */
    public MeetSemiLattice.Member zero() {
        return FALSE;
    }

    /**
     * Returns true if the boolean number is equal to zero, also named
     * false.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(MeetSemiLattice.Member g) {
        return FALSE.equals(g);
    }

    /**
     * Returns the boolean number one.
     *
     * @return DOCUMENT ME!
     */
    public JoinSemiLattice.Member one() {
        return TRUE;
    }

    /**
     * Returns true if the boolean number is equal to one, also named
     * true.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(JoinSemiLattice.Member g) {
        return TRUE.equals(g);
    }

    /**
     * Returns true if one boolean number is the negative of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isComplement(BooleanAlgebra.Member a, BooleanAlgebra.Member b) {
        return a.equals(b.complement());
    }
}
