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

package org.jscience.mathematics.algebraic.groups;

/**
 * This interface defines an abelian group.
 *
 * @author Mark Hale
 * @version 1.0
 */

//an abelian group is a commutative group and should be defined as
//public interface AbelianGroup extends Group {
//    interface Member extends Group.Member {
//    }
//}
//however, it is worth to be defined this way as methods have meaningful names
public interface AbelianGroup {
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
     * Returns true if one member is the negative of the other.
     *
     * @param a a group member
     * @param b a group member
     *
     * @return DOCUMENT ME!
     */
    boolean isNegative(Member a, Member b);

/**
     * This interface defines a member of an abelian group.
     */
    interface Member extends org.jscience.mathematics.Member {
        /**
         * The group composition law.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member add(Member g);

        /**
         * Returns the inverse member.
         *
         * @return DOCUMENT ME!
         */
        Member negate();

        /**
         * The group composition law with inverse.
         *
         * @param g a group member
         *
         * @return DOCUMENT ME!
         */
        Member subtract(Member g);
    }
}
