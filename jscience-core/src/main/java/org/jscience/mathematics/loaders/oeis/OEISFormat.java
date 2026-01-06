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

package org.jscience.mathematics.loaders.oeis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jscience.mathematics.numbers.integers.Integer;

/**
 * Utilities for parsing OEIS internal format (stripped.gz, names.gz, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OEISFormat {

    private static final Pattern STRIPPED_PATTERN = Pattern.compile("^(A\\d{6}) ,(.+)$");

    public static class Entry {
        public final String id;
        public final List<Integer> terms;

        public Entry(String id, List<Integer> terms) {
            this.id = id;
            this.terms = terms;
        }
    }

    /**
     * Parses a line from the 'stripped' OEIS file format.
     */
    public static Entry parseStrippedLine(String line) {
        Matcher matcher = STRIPPED_PATTERN.matcher(line);
        if (matcher.matches()) {
            String id = matcher.group(1);
            String data = matcher.group(2);
            List<Integer> terms = new ArrayList<>();
            for (String part : data.split(",")) {
                try {
                    terms.add(Integer.of(Long.parseLong(part.trim())));
                } catch (NumberFormatException e) {
                    // Ignore malformed numbers
                }
            }
            return new Entry(id, terms);
        }
        return null;
    }
}
