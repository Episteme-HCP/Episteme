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

package org.jscience.io.scientific;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.ResourceWriter;

/**
 * Loader for FASTA format files.
 * Used for DNA/Protein sequence input/output.
 */
public class FastaLoader extends AbstractResourceReader<List<FastaLoader.SequenceRecord>>
        implements ResourceWriter<List<FastaLoader.SequenceRecord>> {

    @Override
    public Class<List<SequenceRecord>> getResourceType() {
        return (Class) List.class;
    }

    @Override
    public String getResourcePath() {
        return null;
    }

    @Override
    protected List<SequenceRecord> loadFromSource(String id) throws Exception {
        return load(id);
    }

    public record SequenceRecord(String header, String sequence) {
    }

    public List<SequenceRecord> load(String filePath) throws IOException {
        try (InputStream is = new FileInputStream(filePath)) {
            return load(is);
        }
    }

    public static List<SequenceRecord> load(InputStream is) throws IOException {
        List<SequenceRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            String header = null;
            StringBuilder sequence = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                if (line.startsWith(">")) {
                    if (header != null) {
                        records.add(new SequenceRecord(header, sequence.toString()));
                    }
                    header = line.substring(1);
                    sequence = new StringBuilder();
                } else {
                    sequence.append(line);
                }
            }
            if (header != null) {
                records.add(new SequenceRecord(header, sequence.toString()));
            }
        }
        return records;
    }

    public void save(List<SequenceRecord> records, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (SequenceRecord record : records) {
                writer.write(">" + record.header());
                writer.newLine();
                // Write sequence in 80-char lines
                String seq = record.sequence();
                for (int i = 0; i < seq.length(); i += 80) {
                    writer.write(seq.substring(i, Math.min(i + 80, seq.length())));
                    writer.newLine();
                }
            }
        }
    }
}
