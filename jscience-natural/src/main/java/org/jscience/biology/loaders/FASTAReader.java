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
import java.util.Collections;
import java.util.List;

/**
 * Reader for FASTA format sequences.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FASTAReader {

    /**
     * Reads sequences from a FASTA stream.
     * 
     * @param inputStream the input stream
     * @param type        the expected type of sequence (DNA, RNA, Protein)
     * @return list of loaded bio sequences
     * @throws IOException on error
     */
    public static List<BioSequence> read(InputStream inputStream, BioSequence.Type type) throws IOException {
        List<BioSequence> sequences = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder currentSeq = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            if (line.startsWith(">")) {
                if (currentSeq.length() > 0) {
                    sequences.add(new BioSequence(currentSeq.toString(), type));
                    currentSeq.setLength(0);
                }
            } else {
                currentSeq.append(line);
            }
        }

        if (currentSeq.length() > 0) {
            sequences.add(new BioSequence(currentSeq.toString(), type));
        }

        return Collections.unmodifiableList(sequences);
    }
}
