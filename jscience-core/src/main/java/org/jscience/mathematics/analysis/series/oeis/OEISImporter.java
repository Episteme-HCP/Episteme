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

package org.jscience.mathematics.analysis.series.oeis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jscience.mathematics.analysis.series.IntegerSequence;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.numbers.integers.Natural;

/**
 * Importer for local OEIS data files.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OEISImporter {

    /**
     * Imports sequences from a 'stripped' format file.
     * 
     * @param file the file to import
     * @return a map of OEIS ID to Sequence
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, IntegerSequence> importStripped(File file) throws IOException {
        Map<String, IntegerSequence> sequences = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;
                OEISFormat.Entry entry = OEISFormat.parseStrippedLine(line);
                if (entry != null) {
                    sequences.put(entry.id, new CachedSequence(entry.id, entry.terms));
                }
            }
        }
        return sequences;
    }

    private static class CachedSequence implements IntegerSequence {
        private final String id;
        private final java.util.List<Integer> terms;

        public CachedSequence(String id, java.util.List<Integer> terms) {
            this.id = id;
            this.terms = terms;
        }

        @Override
        public Integer get(Natural n) {
            int index = n.intValue();
            if (index >= terms.size())
                return null; // Or throw?
            return terms.get(index);
        }

        @Override
        public String getOEISId() {
            return id;
        }
    }
}