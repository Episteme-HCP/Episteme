/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.phyloxml;

import org.jscience.biology.taxonomy.Species;
import org.jscience.biology.evolution.PhylogeneticTree;
import org.jscience.biology.evolution.Clade;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridge for converting PhyloXML DTOs to core JScience evolutionary biology objects.
 */
public class PhyloXMLBridge {

    /**
     * Converts PhyloXML document to JScience PhylogeneticTree.
     */
    public PhylogeneticTree toPhylogeneticTree(PhyloXMLDocument doc) {
        if (doc == null || doc.getPhylogeny() == null) {
            return null;
        }
        
        PhyloXMLPhylogeny phylogeny = doc.getPhylogeny();
        PhylogeneticTree tree = new PhylogeneticTree(phylogeny.getName());
        
        tree.setTrait("phyloxml.rooted", phylogeny.isRooted());
        tree.setTrait("phyloxml.branch_length_unit", phylogeny.getBranchLengthUnit());
        
        if (phylogeny.getClade() != null) {
            tree.setRoot(convertClade(phylogeny.getClade()));
        }
        
        return tree;
    }

    /**
     * Recursively converts PhyloXML clade DTO to core JScience Clade.
     */
    public Clade convertClade(PhyloXMLClade dto) {
        if (dto == null) {
            return null;
        }
        
        Clade clade = new Clade(dto.getName());
        clade.setBranchLength(dto.getBranchLength());
        
        // Transfer confidence values as traits
        for (String type : dto.getConfidenceValues().keySet()) {
            clade.setTrait("confidence." + type, dto.getConfidenceValues().get(type));
        }
        
        // Convert taxonomy to Species if present
        if (dto.getTaxonomy() != null) {
            clade.setSpecies(convertTaxonomy(dto.getTaxonomy()));
        }
        
        // Convert events as traits
        if (dto.getEvents() != null) {
            clade.setTrait("events.speciations", dto.getEvents().getSpeciations());
            clade.setTrait("events.duplications", dto.getEvents().getDuplications());
            clade.setTrait("events.type", dto.getEvents().getType());
        }
        
        // Recursive children
        for (PhyloXMLClade childDto : dto.getChildren()) {
            clade.addChild(convertClade(childDto));
        }
        
        return clade;
    }

    /**
     * Converts PhyloXML taxonomy DTO to JScience Species.
     */
    public Species convertTaxonomy(PhyloXMLTaxonomy dto) {
        if (dto == null) {
            return null;
        }
        
        String commonName = dto.getCommonName();
        String scientificName = dto.getScientificName();
        
        if (scientificName == null || scientificName.isEmpty()) {
            scientificName = dto.getCode();
        }
        if (commonName == null || commonName.isEmpty()) {
            commonName = scientificName;
        }
        
        Species species = new Species(commonName, scientificName);
        species.setTrait("rank", dto.getRank());
        species.setTrait("taxonomy.id", dto.getId());
        species.setTrait("taxonomy.provider", dto.getIdProvider());
        
        return species;
    }

    /**
     * Extracts all species from a PhyloXML tree.
     */
    public List<Species> extractSpecies(PhylogeneticTree tree) {
        List<Species> species = new ArrayList<>();
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
