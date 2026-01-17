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

    // --- Mandatory Abstract Methods (I18n) ---

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.biology");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("writer.fastawriter.name", "FASTA Writer");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("writer.fastawriter.desc", "Writes FASTA format files.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("writer.fastawriter.longdesc", "Writes biological sequences to FASTA format files.");
    }
}
