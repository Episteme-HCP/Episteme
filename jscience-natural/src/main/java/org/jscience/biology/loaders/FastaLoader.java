/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.jscience.io.AbstractLoader;

/**
 * Loads Biological Sequences from FASTA format.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FASTALoader extends AbstractLoader<List<FASTALoader.Sequence>> {

    @Override
    protected List<Sequence> loadFromSource(String resourceId) throws Exception {
        if (resourceId.startsWith("http")) {
            try (InputStream is = new java.net.URI(resourceId).toURL().openStream()) {
                return load(is);
            }
        }
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is == null)
                throw new java.io.IOException("FASTA resource not found: " + resourceId);
            return load(is);
        }
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Sequence>> getResourceType() {
        return (Class<List<Sequence>>) (Class<?>) List.class;
    }

    public static class Sequence {
        public String header;
        public String data; // Nucleotides or Amino Acids

        public Sequence(String header, String data) {
            this.header = header;
            this.data = data;
        }
    }

    public static List<Sequence> load(InputStream is) {
        List<Sequence> sequences = new ArrayList<>();
        if (is == null)
            return sequences;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            String currentHeader = null;
            StringBuilder currentData = new StringBuilder();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                if (line.startsWith(">")) {
                    if (currentHeader != null) {
                        sequences.add(new Sequence(currentHeader, currentData.toString()));
                    }
                    currentHeader = line.substring(1).trim();
                    currentData = new StringBuilder();
                } else {
                    currentData.append(line);
                }
            }
            if (currentHeader != null) {
                sequences.add(new Sequence(currentHeader, currentData.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sequences;
    }
}
