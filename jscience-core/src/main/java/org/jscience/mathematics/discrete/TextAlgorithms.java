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

package org.jscience.mathematics.discrete;

import java.util.ArrayList;
import java.util.List;

/**
 * High-performance bit-parallel text matching algorithms.
 * 
 * <p>
 * References:
 * <ul>
 * <li>Baeza-Yates, R., & Gonnet, G. H. (1992). A new approach to text
 * searching. Communications of the ACM, 35(10), 74-82. (Shift-Or
 * algorithm)</li>
 * <li>Wu, S., & Manber, U. (1992). Fast text searching: allowing errors.
 * Communications of the ACM, 35(10), 83-91. (Shift-And algorithm)</li>
 * </ul>
 * </p>
 */
public class TextAlgorithms {

    /**
     * Finds all exact occurrences of a pattern in a text using the Shift-Or
     * algorithm.
     * Complexity: O(n + m + sigma) where n is text length, m pattern length, sigma
     * alphabet size.
     * Restricted to pattern length <= 64.
     * 
     * @param text    The text to search in
     * @param pattern The pattern to look for
     * @return List of starting positions
     */
    public static List<Integer> shiftOrSearch(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int m = pattern.length();
        if (m == 0 || m > 64)
            return positions;

        long[] b = new long[256];
        for (int i = 0; i < 256; i++)
            b[i] = ~0L;

        for (int i = 0; i < m; i++) {
            b[pattern.charAt(i) & 0xFF] &= ~(1L << i);
        }

        long d = ~0L;
        for (int i = 0; i < text.length(); i++) {
            d = (d << 1) | b[text.charAt(i) & 0xFF];
            if ((d & (1L << (m - 1))) == 0) {
                positions.add(i - m + 1);
            }
        }
        return positions;
    }
}
