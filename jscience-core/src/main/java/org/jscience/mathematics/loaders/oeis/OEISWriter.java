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

import java.io.FileWriter;
import java.io.PrintWriter;

import org.jscience.io.AbstractResourceWriter;
import org.jscience.mathematics.analysis.series.IntegerSequence;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.numbers.integers.Natural;

/**
 * Writer for exporting sequences to OEIS formats.
 * Supports stripped format and b-file format.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OEISWriter extends AbstractResourceWriter<IntegerSequence> {

    private int termCount = 100;
    private OEISOutputFormat format = OEISOutputFormat.STRIPPED;

    public enum OEISOutputFormat {
        STRIPPED, BFILE
    }

    @Override
    public Class<IntegerSequence> getResourceType() {
        return IntegerSequence.class;
    }

    @Override
    public String getResourcePath() {
        return null;
    }

    @Override
    public String getCategory() {
        return "Mathematics";
    }

    @Override
    public String getName() {
        return "OEIS Writer";
    }

    @Override
    public String getDescription() {
        return "Exports sequences to OEIS formats.";
    }

    @Override
    public String getLongDescription() {
        return "Support for stripped and b-file formats used by the On-Line Encyclopedia of Integer Sequences.";
    }

    public OEISWriter() {
    }

    public OEISWriter termCount(int count) {
        this.termCount = count;
        return this;
    }

    public OEISWriter format(OEISOutputFormat format) {
        this.format = format;
        return this;
    }

    @Override
    public void save(IntegerSequence sequence, String destination) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(destination))) {
            if (format == OEISOutputFormat.STRIPPED) {
                exportStripped(sequence, termCount, writer);
            } else {
                exportBFile(sequence, termCount, writer);
            }
        }
    }

    /**
     * Exports a sequence to the 'stripped' format.
     * Format: ID ,term1,term2,term3,...
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
     */
    public static boolean isValidOEISId(String id) {
        if (id == null)
            return false;
        return id.matches("^A\\d{6}$");
    }

    /**
     * Exports a sequence to the b-file format.
     * Format: n a(n)
     */
    public static void exportBFile(IntegerSequence sequence, int count, PrintWriter writer) {
        for (int i = 0; i < count; i++) {
            writer.print(i);
            writer.print(" ");
            writer.println(sequence.get(Natural.of(i)));
        }
    }
}
