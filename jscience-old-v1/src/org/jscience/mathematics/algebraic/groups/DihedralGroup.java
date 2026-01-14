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
 * The DihedralGroup class represents the <i>n</i>th dihedral group.
 * Elements are represented by rotations and reflections.
 *
 * @author Mark Hale
 * @version 1.4
 * @planetmath DihedralGroup
 */
public final class DihedralGroup extends FiniteGroup {
    /**
     * The number of rotations/reflections.
     */
    private final int n;
    /**
     * The identity element.
     */
    private final Member ONE;

    /**
     * Constructs a dihedral group.
     *
     * @param n the number of rotations/reflections
     */
    public DihedralGroup(int n) {
        super(2 * n);
        this.n = n;
        ONE = new Member(0, false);
    }

    /**
     * Returns true if this group is isomorphic to another.
     */
    public boolean equals(Object o) {
        return (o instanceof DihedralGroup) && (((DihedralGroup) o).order == order);
    }

    public String toString() {
        return "D_" + n;
    }

    /**
     * Returns the elements of this group.
     */
    public Group.Member[] getElements() {
        Group.Member elements[] = new Group.Member[order];
        for (int i = 0; i < n; i++) {
            elements[i] = new Member(i, false);
            elements[i + n] = new Member(i, true);
        }
        return elements;
    }

    /**
     * Returns the identity element.
     */
    public Monoid.Member identity() {
        return ONE;
    }

    /**
     * Returns true if the element is the identity element of this group.
     *
     * @param a a group element
     */
    public boolean isIdentity(Monoid.Member a) {
        return a.equals(ONE);
    }

    /**
     * Returns true if one element is the inverse of the other.
     *
     * @param a a group element
     * @param b a group element
     */
    public boolean isInverse(Group.Member a, Group.Member b) {
        return (a instanceof Member) && (b instanceof Member) && a.compose(b).equals(ONE);
    }

    class Member implements Group.Member {
        /**
         * 0 <= rotate < n
         */
        private final int rotate;
        private final boolean reflect;

        public Member(int theta, boolean flip) {
            rotate = (theta < 0) ? theta % n + n : theta % n;
            reflect = flip;
        }

        /**
         * Returns true if this member is equal to another.
         */
        public boolean equals(Object o) {
            return (o instanceof Member) && (rotate == ((Member) o).rotate) && (reflect == ((Member) o).reflect);
        }

        /**
         * The group composition law.
         *
         * @param g a group member
         */
        public Magma.Member compose(Magma.Member g) {
            if (reflect)
                return new Member(rotate - ((Member) g).rotate, !((Member) g).reflect);
            else
                return new Member(rotate + ((Member) g).rotate, ((Member) g).reflect);
        }

        /**
         * Returns the inverse member.
         */
        public Group.Member inverse() {
            return new Member(-rotate, !reflect);
        }
    }
}

