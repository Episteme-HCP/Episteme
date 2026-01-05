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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.ResourceWriter;

/**
 * Loads and saves Biological Sequences from FASTA format.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FASTALoader extends AbstractResourceReader<List<FASTALoader.Sequence>>
        implements ResourceWriter<List<FASTALoader.Sequence>> {

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

    @Override
    public void save(List<Sequence> sequences, String destination) throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(destination)))) {
            for (Sequence seq : sequences) {
                pw.println(">" + seq.header);
                String data = seq.data;
                for (int i = 0; i < data.length(); i += 60) {
                    pw.println(data.substring(i, Math.min(i + 60, data.length())));
                }
            }
        }
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
