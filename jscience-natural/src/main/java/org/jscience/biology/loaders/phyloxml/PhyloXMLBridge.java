/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

import org.jscience.biology.taxonomy.Species;
import org.jscience.biology.taxonomy.ConservationStatus;
import org.jscience.biology.evolution.PhylogeneticTree;
import org.jscience.biology.evolution.Clade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridge for converting PhyloXML DTOs to core JScience evolutionary biology objects.
 * <p>
 * PhyloXML is the XML standard for phylogenetic trees. This bridge converts
 * parsed PhyloXML structures to the core JScience evolution and taxonomy domain.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * PhyloXML → PhyloXMLReader → Clade DTOs → PhyloXMLBridge → Core JScience Objects
 *                                                           ├── PhylogeneticTree
 *                                                           ├── Clade
 *                                                           └── Species
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PhyloXMLBridge {

    /**
     * Converts PhyloXML phylogeny to JScience PhylogeneticTree.
     *
     * @param phyloModel the parsed PhyloXML model
     * @return a PhylogeneticTree object
     */
    public PhylogeneticTree toPhylogeneticTree(PhyloXMLModel phyloModel) {
        if (phyloModel == null || phyloModel.getPhylogeny() == null) {
            return null;
        }
        
        PhyloXMLPhylogeny phylogeny = phyloModel.getPhylogeny();
        PhylogeneticTree tree = new PhylogeneticTree(phylogeny.getName());
        
        tree.setTrait("phyloxml.rooted", phylogeny.isRooted());
        tree.setTrait("phyloxml.rerootable", phylogeny.isRerootable());
        
        // Convert root clade
        if (phylogeny.getClade() != null) {
            Clade root = convertClade(phylogeny.getClade());
            tree.setRoot(root);
        }
        
        return tree;
    }

    /**
     * Recursively converts PhyloXML clade to JScience Clade.
     */
    public Clade convertClade(PhyloXMLClade phyloClade) {
        if (phyloClade == null) {
            return null;
        }
        
        String name = phyloClade.getName();
        Clade clade = new Clade(name != null ? name : "unnamed");
        
        // Set branch length
        if (phyloClade.getBranchLength() != null) {
            clade.setBranchLength(phyloClade.getBranchLength());
        }
        
        // Set confidence/bootstrap
        if (phyloClade.getConfidence() != null) {
            clade.setTrait("confidence", phyloClade.getConfidence());
            clade.setTrait("confidence.type", phyloClade.getConfidenceType());
        }
        
        // Convert taxonomy to Species if present
        if (phyloClade.getTaxonomy() != null) {
            Species species = convertTaxonomy(phyloClade.getTaxonomy());
            clade.setSpecies(species);
        }
        
        // Convert events (speciation, duplication, etc.)
        if (phyloClade.getEvents() != null) {
            clade.setTrait("events.type", phyloClade.getEvents().getType());
            clade.setTrait("events.duplications", phyloClade.getEvents().getDuplications());
            clade.setTrait("events.speciations", phyloClade.getEvents().getSpeciations());
        }
        
        // Recursively convert child clades
        if (phyloClade.getClades() != null) {
            for (PhyloXMLClade child : phyloClade.getClades()) {
                Clade childClade = convertClade(child);
                if (childClade != null) {
                    clade.addChild(childClade);
                }
            }
        }
        
        return clade;
    }

    /**
     * Converts PhyloXML taxonomy to JScience Species.
     */
    public Species convertTaxonomy(PhyloXMLTaxonomy taxonomy) {
        if (taxonomy == null) {
            return null;
        }
        
        String commonName = taxonomy.getCommonName();
        String scientificName = taxonomy.getScientificName();
        
        if (scientificName == null || scientificName.isEmpty()) {
            scientificName = taxonomy.getCode();
        }
        if (commonName == null || commonName.isEmpty()) {
            commonName = scientificName;
        }
        
        Species species = new Species(commonName, scientificName);
        
        // Set taxonomic ranks
        if (taxonomy.getRank() != null) {
            species.setTrait("rank", taxonomy.getRank());
        }
        
        // Set taxonomy identifiers
        if (taxonomy.getId() != null) {
            species.setTrait("taxonomy.id", taxonomy.getId());
            species.setTrait("taxonomy.id.provider", taxonomy.getIdProvider());
        }
        
        return species;
    }

    /**
     * Extracts all species from a PhyloXML tree.
     */
    public List<Species> extractSpecies(PhyloXMLModel phyloModel) {
        List<Species> species = new ArrayList<>();
        PhylogeneticTree tree = toPhylogeneticTree(phyloModel);
        if (tree != null && tree.getRoot() != null) {
            collectSpecies(tree.getRoot(), species);
        }
        return species;
    }

    private void collectSpecies(Clade clade, List<Species> species) {
        if (clade.getSpecies() != null) {
            species.add(clade.getSpecies());
        }
        for (Clade child : clade.getChildren()) {
            collectSpecies(child, species);
        }
    }
}
