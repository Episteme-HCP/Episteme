package org.jscience.biology.io;

import org.jscience.biology.Taxon;
import org.jscience.io.ResourceReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhylogeneticTreeReader implements ResourceReader<Taxon> {

    @Override
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
                double coi = Double.parseDouble(p[3]);
                double rna = Double.parseDouble(p[4]);
                double cytb = Double.parseDouble(p[5]);
                
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
        return "Phylogenetic CSV Reader";
    }

    @Override
    public List<String> getSupportedExtensions() {
        return List.of("csv");
    }
}
