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

package org.jscience.biology.loaders;

import org.jscience.biology.genetics.BioSequence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for FASTA format files.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FastaParser {

    /**
     * Parses a FASTA input stream.
     *
     * @param inputStream the input stream containing FASTA data
     * @return a list of BioSequence objects
     * @throws IOException if an I/O error occurs
     */
    public List<BioSequence> parse(InputStream inputStream) throws IOException {
        List<BioSequence> sequences = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder sequenceBuilder = new StringBuilder();
            String currentHeader = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                if (line.startsWith(">")) {
                    if (currentHeader != null) {
                        createAndAddSequence(sequences, sequenceBuilder.toString());
                        sequenceBuilder.setLength(0);
                    }
                    currentHeader = line.substring(1).trim();
                } else {
                    sequenceBuilder.append(line);
                }
            }

            if (currentHeader != null) {
                createAndAddSequence(sequences, sequenceBuilder.toString());
            }
        }
        return sequences;
    }

    private void createAndAddSequence(List<BioSequence> sequences, String sequence) {
        if (sequence.isEmpty())
            return;
        BioSequence.Type type = guessType(sequence);
        try {
            sequences.add(new BioSequence(sequence, type));
        } catch (IllegalArgumentException e) {
            // Fallback: try Protein if DNA failed
            if (type != BioSequence.Type.PROTEIN) {
                try {
                    sequences.add(new BioSequence(sequence, BioSequence.Type.PROTEIN));
                } catch (Exception ex) {
                    // ignore invalid
                }
            }
        }
    }

    private BioSequence.Type guessType(String sequence) {
        String s = sequence.toUpperCase();
        if (s.contains("U"))
            return BioSequence.Type.RNA;
        // Check for non-DNA chars
        for (char c : s.toCharArray()) {
            if ("ACGTN".indexOf(c) == -1)
                return BioSequence.Type.PROTEIN;
        }
        return BioSequence.Type.DNA;
    }
}
