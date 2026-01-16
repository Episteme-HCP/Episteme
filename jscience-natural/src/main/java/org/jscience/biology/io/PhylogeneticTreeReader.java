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
        return I18n.getInstance().get("reader.phylogeny.name", "Phylogenetic Tree Reader");
    }

    @Override
    public String getCategory() { 
        return I18n.getInstance().get("reader.phylogeny.category", "Biology"); 
    }

    @Override
    public String getDescription() { 
        return I18n.getInstance().get("reader.phylogeny.desc", "Reads phylogenetic trees from CSV format."); 
    }

    @Override public String getResourcePath() { return "/data/phylogeny/"; }
    @Override public Class<Taxon> getResourceType() { return Taxon.class; }
}
