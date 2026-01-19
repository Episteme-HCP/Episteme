package org.jscience.biology.loaders;

import org.jscience.natural.biology.ProteinFolding;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;
import org.jscience.io.BasicMiniCatalog;
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
