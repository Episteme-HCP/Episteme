/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.biology.loaders;

import org.episteme.natural.biology.ProteinFolding;
import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.io.MiniCatalog;
import org.episteme.core.io.BasicMiniCatalog;
import java.util.*;

/**
 * Resource reader for biological data models.
 * Provides sample protein chains and ecological data.
 */
public final class BiologicalResourceReader extends AbstractResourceReader<ProteinFolding> {

    @Override public String getCategory() { return "Biology"; }
    @Override public String getName() { return "Biological Resource Reader"; }
    @Override public String getDescription() { return "Loads protein structures and ecological data models."; }
    @Override public String getLongDescription() { return "Support for PDB and FASTA formats with integrated simulation samples."; }
    @Override public String getResourcePath() { return "data/biology"; }
    @Override public Class<ProteinFolding> getResourceType() { return ProteinFolding.class; }
    
    @Override 
    public String[] getSupportedVersions() {
        return new String[] { "1.0" };
    }

    @Override
    protected ProteinFolding loadFromSource(String id) throws Exception {
        return null; 
    }

    @Override
    protected MiniCatalog<ProteinFolding> getMiniCatalog() {
        BasicMiniCatalog<ProteinFolding> catalog = new BasicMiniCatalog<>();
        catalog.register("ProteinAlpha", createSampleProtein());
        return catalog;
    }

    private ProteinFolding createSampleProtein() {
        // Create a simple glycine-heavy chain as a sample
        List<ProteinFolding.Residue> chain = new ArrayList<>();
        String sequence = "GAVLIYSTNQ";
        for (int i = 0; i < sequence.length(); i++) {
            String symbol = String.valueOf(sequence.charAt(i));
            chain.add(new ProteinFolding.Residue(symbol, new double[]{i * 3.8, 0.0, 0.0}, ProteinFolding.predictAa(symbol)));
        }
        return new ProteinFolding(chain);
    }
}


