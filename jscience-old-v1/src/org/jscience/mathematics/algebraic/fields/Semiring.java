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

package org.jscience.mathematics.algebraic.fields;


//import org.jscience.mathematics.algebraic.groups.AbelianGroup;
/**
 * This interface defines a semiring (similar to a ring but without additive
 * inverses).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//an semiring is a set with a commutative monoid operation as addition, together with a monoid operation as multiplication, satisfying distributivity
//and should be defined as
//public interface Semiring extends Monoid {
//   Member one();
//   boolean isOne(Member r);
//    interface Member extends Monoid.Member {
//         Member multiply(Member r);
//    }
//}
//however, it is worth to be defined this way as methods have meaningful names
//also see AbelianGroup.
public interface Semiring {
    /**
     * Returns the identity element.
     *
     * @return DOCUMENT ME!
     */
    Member zero();

    /**
     * Returns true if the member is the identity element of this
     * group.
     *
     * @param g a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isZero(Member g);

    /**
     * Returns the unit element.
     *
     * @return DOCUMENT ME!
     */
    Member one();

    /**
     * Returns true if the member is the unit element.
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean isOne(Member r);

/**
     * This interface defines a member of a semiring.
     */
    interface Member extends org.jscience.mathematics.Member {
        /**
         * The addition law.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member add(Member g);

        /**
         * The multiplication law.
         *
         * @param r a ring member
         *
         * @return DOCUMENT ME!
         */
        Member multiply(Member r);
    }
}
