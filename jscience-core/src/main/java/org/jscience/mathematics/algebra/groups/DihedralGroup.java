/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;

/**
 * Represents a Dihedral Group D_n of order 2n.
 * <p>
 * Elements are symmetries of a regular n-gon: rotations and reflections.
 * Represented as s^a * r^b where a in {0, 1} and b in {0, ..., n-1}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DihedralGroup implements Group<DihedralGroup.Element> {

    private final int n;
    private final Element identity;

    public DihedralGroup(int n) {
        if (n < 3) {
            throw new IllegalArgumentException("Order n must be >= 3 for a geometric dihedral group");
        }
        this.n = n;
        this.identity = new Element(false, 0);
    }

    public int getN() {
        return n;
    }

    @Override
    public Element operate(Element left, Element right) {
        // left = s^a r^b
        // right = s^c r^d
        // If a=0: r^b * s^c r^d
        // If c=0: r^b r^d = r^{b+d}
        // If c=1: r^b s r^d = s r^{-b} r^d = s r^{d-b}
        // If a=1: s r^b * s^c r^d
        // If c=0: s r^b r^d = s r^{b+d}
        // If c=1: s r^b s r^d = s s r^{-b} r^d = r^{d-b}

        boolean s_new;
        int r_new;

        if (!left.hasReflection) { // a=0
            if (!right.hasReflection) { // c=0
                s_new = false;
                r_new = (left.rotation + right.rotation) % n;
            } else { // c=1
                s_new = true;
                r_new = (right.rotation - left.rotation) % n;
            }
        } else { // a=1
            if (!right.hasReflection) { // c=0
                s_new = true;
                r_new = (left.rotation + right.rotation) % n;
            } else { // c=1
                s_new = false;
                r_new = (right.rotation - left.rotation) % n;
            }
        }

        if (r_new < 0)
            r_new += n;
        return new Element(s_new, r_new);
    }

    @Override
    public Element identity() {
        return identity;
    }

    @Override
    public Element inverse(Element element) {
        // If s=0: (r^b)^-1 = r^{-b} = r^{n-b}
        // If s=1: (s r^b)^-1 = s r^b (since reflections are self-inverse? No)
        // (s r^b) * (s r^b) = s r^b s r^b = s s r^{-b} r^b = 1. Yes, self-inverse.
        if (!element.hasReflection) {
            int r = (-element.rotation) % n;
            if (r < 0)
                r += n;
            return new Element(false, r);
        } else {
            return element;
        }
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public String description() {
        return "Dihedral Group D_" + n + " (Order " + (2 * n) + ")";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Element element) {
        return element != null && element.rotation >= 0 && element.rotation < n;
    }

    /**
     * An element of the Dihedral group.
     */
    public class Element {
        public final boolean hasReflection; // s
        public final int rotation; // r^k

        public Element(boolean hasReflection, int rotation) {
            this.hasReflection = hasReflection;
            this.rotation = rotation;
        }

        @Override
        public String toString() {
            if (!hasReflection && rotation == 0)
                return "1";
            StringBuilder sb = new StringBuilder();
            if (hasReflection)
                sb.append("s");
            if (rotation > 0)
                sb.append("r^").append(rotation);
            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Element))
                return false;
            Element other = (Element) obj;
            return this.hasReflection == other.hasReflection && this.rotation == other.rotation;
        }

        @Override
        public int hashCode() {
            return (hasReflection ? 1 : 0) * 31 + rotation;
        }
    }
}


