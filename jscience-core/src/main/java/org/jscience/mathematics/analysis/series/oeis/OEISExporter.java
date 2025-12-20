/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.analysis.series.oeis;

import java.io.PrintWriter;

import org.jscience.mathematics.analysis.series.IntegerSequence;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.numbers.integers.Natural;

/**
 * Exporter for sequences to OEIS formats.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OEISExporter {

    /**
     * Exports a sequence to the 'stripped' format.
     * Format: ID ,term1,term2,term3,...
     * 
     * @param sequence the sequence to export
     * @param count    number of terms to export
     * @param writer   the output writer
     */
    public static void exportStripped(IntegerSequence sequence, int count, PrintWriter writer) {
        String id = sequence.getOEISId();
        if (id == null)
            throw new IllegalArgumentException(
                    "Sequence defined with no OEIS ID cannot be exported to stripped format.");

        if (!isValidOEISId(id))
            throw new IllegalArgumentException("Invalid OEIS ID: " + id);

        writer.print(id);
        writer.print(" ,");

        for (int i = 0; i < count; i++) {
            if (i > 0)
                writer.print(",");
            Integer val = sequence.get(Natural.of(i));
            writer.print(val);
        }
        writer.println();
    }

    /**
     * Validates an OEIS ID format (A followed by 6 digits).
     * 
     * @param id the ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidOEISId(String id) {
        if (id == null)
            return false;
        return id.matches("^A\\d{6}$");
    }

    /**
     * Exports a sequence to the internal format (b-file style).
     * Format: n a(n)
     * 
     * @param sequence the sequence to export
     * @param count    number of terms
     * @param writer   the output writer
     */
    public static void exportBFile(IntegerSequence sequence, int count, PrintWriter writer) {
        for (int i = 0; i < count; i++) {
            writer.print(i); // Or 1-based? OEIS usually 1-based for b-files but depends on offset.
            // We'll use 0-based index for now or sequence domain.
            writer.print(" ");
            writer.println(sequence.get(Natural.of(i)));
        }
    }
}
