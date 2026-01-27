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

package org.jscience.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a hierarchical numbering (e.g., "1.0.1", "Article 42.1").
 * Used for law articles, software versions, and other structured identifiers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Numbering implements Comparable<Numbering>, Serializable {

    private static final long serialVersionUID = 1L;

    private final int[] values;

    /**
     * Creates a new Numbering from an array of integers.
     *
     * @param values components of the numbering (must be non-negative).
     */
    public Numbering(int... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Numbering must have at least one component.");
        }
        for (int v : values) {
            if (v < 0) throw new IllegalArgumentException("Negative values not allowed in Numbering.");
        }
        this.values = Arrays.copyOf(values, values.length);
    }

    /**
     * Parses a dot-separated string into a Numbering.
     *
     * @param s dot-separated string (e.g., "1.2.3").
     * @return new Numbering object.
     */
    public static Numbering parse(String s) {
        if (s == null || s.isEmpty()) return new Numbering(0);
        String[] parts = s.split("\\.");
        int[] vals = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                vals[i] = Integer.parseInt(parts[i].trim().replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                vals[i] = 0;
            }
        }
        return new Numbering(vals);
    }

    public int[] getValues() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public String toString() {
        return Arrays.stream(values)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("."));
    }

    @Override
    public int compareTo(Numbering other) {
        int len = Math.max(values.length, other.values.length);
        for (int i = 0; i < len; i++) {
            int v1 = (i < values.length) ? values[i] : 0;
            int v2 = (i < other.values.length) ? other.values[i] : 0;
            if (v1 != v2) return Integer.compare(v1, v2);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Numbering)) return false;
        Numbering that = (Numbering) o;
        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    /**
     * Returns the next numbering at the current depth (e.g., 1.2 -> 1.3).
     */
    public Numbering getNext() {
        int[] next = Arrays.copyOf(values, values.length);
        next[next.length - 1]++;
        return new Numbering(next);
    }

    /**
     * Adds a minor version (e.g., 1.2 -> 1.2.0).
     */
    public Numbering addMinor() {
        int[] next = Arrays.copyOf(values, values.length + 1);
        next[next.length - 1] = 0;
        return new Numbering(next);
    }
}
