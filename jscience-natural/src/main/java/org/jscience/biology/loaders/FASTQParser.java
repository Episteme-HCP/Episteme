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

package org.jscience.biology.loaders;

import org.jscience.biology.genetics.BioSequence;
import org.jscience.io.AbstractResourceReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for FASTQ format files (includes quality scores).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FASTQParser extends AbstractResourceReader<List<BioSequence>> {

    @Override
    protected List<BioSequence> loadFromSource(String resourceId) throws Exception {
        if (resourceId.startsWith("http")) {
            try (InputStream is = new java.net.URI(resourceId).toURL().openStream()) {
                return parse(is);
            }
        }
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is == null)
                throw new java.io.IOException("FASTQ resource not found: " + resourceId);
            return parse(is);
        }
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<BioSequence>> getResourceType() {
        return (Class<List<BioSequence>>) (Class<?>) List.class;
    }

    /**
     * Parses a FASTQ input stream.
     *
     * @param inputStream the input stream containing FASTQ data
     * @return a list of BioSequence objects (quality scores currently stored in
     *         metadata if supported, or ignored for basic BioSequence)
     * @throws IOException if an I/O error occurs
     */
    public List<BioSequence> parse(InputStream inputStream) throws IOException {
        List<BioSequence> sequences = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                if (line.startsWith("@")) {
                    // String header = line.substring(1); // Not used currently in construction
                    String sequence = reader.readLine();
                    String plus = reader.readLine(); // " + " line
                    String quality = reader.readLine();

                    if (sequence != null && plus != null && quality != null) {
                        try {
                            BioSequence.Type type = guessType(sequence.trim());
                            BioSequence bioSeq = new BioSequence(sequence.trim(), type);
                            sequences.add(bioSeq);
                        } catch (Exception e) {
                            // Ignore invalid sequences or provide fallback
                            try {
                                // Fallback to Protein if DNA failed (rare for FASTQ but possible)
                                BioSequence bioSeq = new BioSequence(sequence.trim(), BioSequence.Type.PROTEIN);
                                sequences.add(bioSeq);
                            } catch (Exception ex) {
                                // ignore
                            }
                        }
                    }
                }
            }
        }
        return sequences;
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

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.biology");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.fastqparser.name");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.fastqparser.description");
    }
}
