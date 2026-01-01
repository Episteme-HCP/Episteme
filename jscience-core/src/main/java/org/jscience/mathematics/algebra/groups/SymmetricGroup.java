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
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Represents the Symmetric Group S_n (group of all permutations of n elements).
 * <p>
 * The order of S_n is n!.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SymmetricGroup implements Group<SymmetricGroup.Permutation> {

    private final int n;
    private final Permutation identity;

    public SymmetricGroup(int n) {
        if (n < 1)
            throw new IllegalArgumentException("Degree must be at least 1");
        this.n = n;
        int[] id = IntStream.range(0, n).toArray();
        this.identity = new Permutation(id);
    }

    @Override
    public Permutation operate(Permutation left, Permutation right) {
        return left.compose(right);
    }

    @Override
    public Permutation identity() {
        return identity;
    }

    @Override
    public Permutation inverse(Permutation element) {
        return element.inverse();
    }

    @Override
    public boolean isCommutative() {
        return n <= 2;
    }

    @Override
    public String description() {
        return "Symmetric Group S_" + n;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Permutation element) {
        return element != null && element.size() == n;
    }

    /**
     * Represents a permutation of {0, 1, ..., n-1}.
     */
    public static class Permutation {
        private final int[] map;

        public Permutation(int[] map) {
            this.map = Arrays.copyOf(map, map.length);
            // Validation omitted for brevity, but should check if it's a valid bijection
        }

        public int size() {
            return map.length;
        }

        public int apply(int i) {
            return map[i];
        }

        public Permutation compose(Permutation other) {
            if (this.size() != other.size())
                throw new IllegalArgumentException("Dimension mismatch");
            int[] newMap = new int[this.size()];
            for (int i = 0; i < newMap.length; i++) {
                newMap[i] = this.apply(other.apply(i));
            }
            return new Permutation(newMap);
        }

        public Permutation inverse() {
            int[] inv = new int[map.length];
            for (int i = 0; i < map.length; i++) {
                inv[map[i]] = i;
            }
            return new Permutation(inv);
        }

        @Override
        public String toString() {
            return Arrays.toString(map);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Permutation))
                return false;
            return Arrays.equals(this.map, ((Permutation) obj).map);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(map);
        }
    }
}


