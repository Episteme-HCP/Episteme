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

package org.jscience.biology.io;

import org.jscience.biology.Taxon;
import org.jscience.io.AbstractResourceReader;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.ui.i18n.I18n;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads phylogenetic trees from CSV.
 * Uses Real for genetic distance or confidence values.
 
 * <p>
 * <b>Reference:</b><br>
 * Holland, J. H. (1975). <i>Adaptation in Natural and Artificial Systems</i>. University of Michigan Press.
 * </p>
 *
 */
public class PhylogeneticTreeReader extends AbstractResourceReader<Taxon> {

    @Override
    protected Taxon loadFromSource(String id) throws Exception {
        InputStream is = getClass().getResourceAsStream(id);
        if (is == null) is = new java.io.FileInputStream(id);
        return read(is);
    }

    public Taxon read(InputStream input) throws Exception {
        Map<String, Taxon> taxons = new HashMap<>();
        Taxon root = null;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] p = line.split(",");
                if (p.length < 6) continue;
                
                String id = p[0].trim();
                String pid = p[1].trim();
                String name = p[2].trim();
                Real coi = Real.of(p[3]);
                Real rna = Real.of(p[4]);
                Real cytb = Real.of(p[5]);
                
                Taxon t = new Taxon(id, pid, name, coi, rna, cytb);
                taxons.put(id, t);
                
                if (pid.isEmpty()) {
                    root = t;
                }
            }
        }
        
        // Build Hierarchy
        for (Taxon t : taxons.values()) {
            if (t.getParentId() != null && !t.getParentId().isEmpty()) {
                Taxon parent = taxons.get(t.getParentId());
                if (parent != null) {
                    parent.addChild(t);
                }
            }
        }
        
        return root;
    }
    
    @Override
    public String getName() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.phylogenetictreereader.name", "Phylogenetic Tree Reader");
    }

    @Override
    public String getCategory() { 
        return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology"); 
    }

    @Override
    public String getDescription() { 
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.phylogenetictreereader.desc", "Reads phylogenetic trees from CSV format."); 
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.phylogenetictreereader.longdesc", "Parses phylogenetic tree data from CSV, reconstructing hierarchical taxon relationships.");
    }

    @Override public String getResourcePath() { return "/data/phylogeny/"; }
    @Override public Class<Taxon> getResourceType() { return Taxon.class; }
}
