/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.biology.loaders;

import org.jscience.io.AbstractResourceWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Writer for FASTA files.
 */
public class FASTAWriter extends AbstractResourceWriter<List<FASTAReader.Sequence>> {

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<FASTAReader.Sequence>> getResourceType() {
        return (Class<List<FASTAReader.Sequence>>) (Class<?>) List.class;
    }

    @Override
    public void save(List<FASTAReader.Sequence> sequences, String destination) throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(destination)))) {
            for (FASTAReader.Sequence seq : sequences) {
                pw.println(">" + seq.header);
                String data = seq.data;
                for (int i = 0; i < data.length(); i += 60) {
                    pw.println(data.substring(i, Math.min(i + 60, data.length())));
                }
            }
        }
    }
}
