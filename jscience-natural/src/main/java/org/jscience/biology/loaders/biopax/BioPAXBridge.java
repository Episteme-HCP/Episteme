/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import org.jscience.biology.Pathway;
import org.jscience.biology.BioChemicalReaction;
import org.jscience.chemistry.Molecule;
import org.jscience.biology.macromolecules.Protein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridge for converting BioPAX DTOs to core JScience biological pathway objects.
 * <p>
 * BioPAX (Biological Pathway Exchange) is the standard ontology for biological
 * pathway data. This bridge converts parsed BioPAX data to JScience domain objects.
 * </p>
 *
 * <h2>Architecture</h2>
 * <pre>
 * BioPAX OWL/XML → BioPAXReader → BioPAX DTOs → BioPAXBridge → Core Objects
 *                                                              ├── Pathway
 *                                                              ├── BioChemicalReaction
 *                                                              ├── Protein
 *                                                              └── Molecule
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioPAXBridge {

    /**
     * Converts BioPAX pathway to JScience Pathway.
     *
     * @param biopaxPathway the parsed BioPAX pathway
     * @return a Pathway object with reactions and participants
     */
    public Pathway toPathway(BioPAXPathway biopaxPathway) {
        if (biopaxPathway == null) {
            return null;
        }
        
        Pathway pathway = new Pathway(biopaxPathway.getDisplayName());
        pathway.setTrait("biopax.rdf.id", biopaxPathway.getRdfId());
        pathway.setTrait("biopax.organism", biopaxPathway.getOrganism());
        pathway.setTrait("biopax.data.source", biopaxPathway.getDataSource());
        
        // Convert pathway steps/reactions
        if (biopaxPathway.getPathwayComponents() != null) {
            for (BioPAXInteraction interaction : biopaxPathway.getPathwayComponents()) {
                if (interaction instanceof BioPAXBiochemicalReaction) {
                    BioChemicalReaction reaction = convertReaction((BioPAXBiochemicalReaction) interaction);
                    if (reaction != null) {
                        pathway.addReaction(reaction);
                    }
                }
            }
        }
        
        return pathway;
    }

    /**
     * Converts BioPAX biochemical reaction to JScience BioChemicalReaction.
     */
    public BioChemicalReaction convertReaction(BioPAXBiochemicalReaction biopaxReaction) {
        if (biopaxReaction == null) {
            return null;
        }
        
        String name = biopaxReaction.getDisplayName();
        if (name == null) {
            name = biopaxReaction.getRdfId();
        }
        
        BioChemicalReaction reaction = new BioChemicalReaction(name);
        reaction.setTrait("biopax.rdf.id", biopaxReaction.getRdfId());
        reaction.setTrait("reversible", biopaxReaction.getConversionDirection());
        
        // Convert left (reactants)
        if (biopaxReaction.getLeft() != null) {
            for (BioPAXPhysicalEntity entity : biopaxReaction.getLeft()) {
                Molecule mol = convertSmallMolecule(entity);
                if (mol != null) {
                    reaction.addReactant(mol, entity.getStoichiometry());
                }
            }
        }
        
        // Convert right (products)
        if (biopaxReaction.getRight() != null) {
            for (BioPAXPhysicalEntity entity : biopaxReaction.getRight()) {
                Molecule mol = convertSmallMolecule(entity);
                if (mol != null) {
                    reaction.addProduct(mol, entity.getStoichiometry());
                }
            }
        }
        
        // Convert catalysts (enzymes)
        if (biopaxReaction.getCatalysis() != null) {
            for (BioPAXCatalysis catalysis : biopaxReaction.getCatalysis()) {
                if (catalysis.getController() != null) {
                    Protein enzyme = convertProtein(catalysis.getController());
                    if (enzyme != null) {
                        reaction.addCatalyst(enzyme);
                    }
                }
            }
        }
        
        // Add EC number if available
        if (biopaxReaction.getEcNumber() != null) {
            reaction.setTrait("ec.number", biopaxReaction.getEcNumber());
        }
        
        return reaction;
    }

    /**
     * Converts BioPAX small molecule to JScience Molecule.
     */
    public Molecule convertSmallMolecule(BioPAXPhysicalEntity entity) {
        if (entity == null) {
            return null;
        }
        
        String name = entity.getDisplayName();
        if (name == null) {
            name = entity.getRdfId();
        }
        
        Molecule mol = new Molecule(name);
        mol.setTrait("biopax.rdf.id", entity.getRdfId());
        
        // Add chemical identifiers
        if (entity.getChemicalFormula() != null) {
            mol.setTrait("formula", entity.getChemicalFormula());
        }
        if (entity.getSmiles() != null) {
            mol.setTrait("smiles", entity.getSmiles());
        }
        if (entity.getInchi() != null) {
            mol.setTrait("inchi", entity.getInchi());
        }
        
        // Add database cross-references
        if (entity.getXrefs() != null) {
            for (BioPAXXref xref : entity.getXrefs()) {
                mol.setTrait("xref." + xref.getDb(), xref.getId());
            }
        }
        
        return mol;
    }

    /**
     * Converts BioPAX protein to JScience Protein.
     */
    public Protein convertProtein(BioPAXPhysicalEntity entity) {
        if (entity == null) {
            return null;
        }
        
        String name = entity.getDisplayName();
        if (name == null) {
            name = entity.getRdfId();
        }
        
        Protein protein = new Protein(name);
        protein.setTrait("biopax.rdf.id", entity.getRdfId());
        
        // Add protein-specific data
        if (entity.getSequence() != null) {
            protein.setSequence(entity.getSequence());
        }
        
        // Add database cross-references
        if (entity.getXrefs() != null) {
            for (BioPAXXref xref : entity.getXrefs()) {
                protein.setTrait("xref." + xref.getDb(), xref.getId());
            }
        }
        
        return protein;
    }

    /**
     * Extracts all pathways from a BioPAX model.
     */
    public List<Pathway> toPathways(BioPAXModel model) {
        List<Pathway> pathways = new ArrayList<>();
        if (model != null && model.getPathways() != null) {
            for (BioPAXPathway bp : model.getPathways()) {
                Pathway p = toPathway(bp);
                if (p != null) {
                    pathways.add(p);
                }
            }
        }
        return pathways;
    }
}
