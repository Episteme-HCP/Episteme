/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders;

import org.jscience.biology.genetics.BioSequence;
import org.jscience.io.AbstractLoader;
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
public class FASTAParser extends AbstractLoader<List<BioSequence>> {

    @Override
    protected List<BioSequence> loadFromSource(String resourceId) throws Exception {
        if (resourceId.startsWith("http")) {
            try (InputStream is = new java.net.URI(resourceId).toURL().openStream()) {
                return parse(is);
            }
        }
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is == null)
                throw new java.io.IOException("FASTA resource not found: " + resourceId);
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
        return (Class) List.class;
    }

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
