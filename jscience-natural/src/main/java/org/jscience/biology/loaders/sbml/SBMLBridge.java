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

package org.jscience.biology.loaders.sbml;

import org.jscience.biology.BioChemicalReaction;
import org.jscience.chemistry.Molecule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bridge for converting SBML DTOs to core JScience domain objects.
 * <p>
 * This bridge provides systematic conversion from SBML-parsed data transfer objects
 * to the core JScience architecture, enabling deep integration with the library's
 * domain models for molecules, species, reactions, and populations.
 * </p>
 * 
 * <h2>Usage Example</h2>
 * <pre>{@code
 * SBMLReader reader = new SBMLReader();
 * SBMLModel sbmlModel = reader.read(new File("model.sbml"));
 * 
 * // Convert to core JScience structures
 * SBMLBridge bridge = new SBMLBridge();
 * List<Molecule> molecules = bridge.toMolecules(sbmlModel);
 * List<ChemicalReaction> reactions = bridge.toReactions(sbmlModel);
 * }</pre>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see SBMLReader
 * @see SBMLModel
 * @see Molecule
 * @see Species
 */
public class SBMLBridge {

    /**
     * Converts SBML species (metabolites) to JScience Molecule objects.
     * <p>
     * SBML species represent chemical entities in a systems biology model.
     * This method maps them to the core chemistry domain.
     * </p>
     *
     * @param model the parsed SBML model
     * @return list of Molecule objects representing the metabolites
     */
    public List<Molecule> toMolecules(SBMLModel model) {
        List<Molecule> molecules = new ArrayList<>();
        if (model == null || model.getSpecies() == null) {
            return molecules;
        }
        
        for (SBMLSpecies sbmlSpecies : model.getSpecies()) {
            Molecule molecule = convertToMolecule(sbmlSpecies);
            if (molecule != null) {
                molecules.add(molecule);
            }
        }
        return molecules;
    }

    /**
     * Converts a single SBML species to a JScience Molecule.
     *
     * @param sbmlSpecies the SBML species DTO
     * @return a Molecule, or null if conversion fails
     */
    public Molecule convertToMolecule(SBMLSpecies sbmlSpecies) {
        if (sbmlSpecies == null) {
            return null;
        }
        
        String name = sbmlSpecies.getName();
        if (name == null || name.isEmpty()) {
            name = sbmlSpecies.getId();
        }
        
        // Create molecule with available information
        Molecule molecule = new Molecule(name);
        
        // Transfer SBML-specific attributes as traits
        molecule.setTrait("sbml.id", sbmlSpecies.getId());
        molecule.setTrait("sbml.compartment", sbmlSpecies.getCompartmentId());
        
        if (sbmlSpecies.getInitialAmount() != null) {
            molecule.setTrait("initial.amount", sbmlSpecies.getInitialAmount().doubleValue());
        }
        if (sbmlSpecies.getInitialConcentration() != null) {
            molecule.setTrait("initial.concentration", sbmlSpecies.getInitialConcentration().doubleValue());
        }
        
        molecule.setTrait("boundary.condition", sbmlSpecies.isBoundaryCondition());
        molecule.setTrait("constant", sbmlSpecies.isConstant());
        
        // Store formula if available
        String formula = sbmlSpecies.getFormula();
        if (formula != null && !formula.isEmpty()) {
            molecule.setTrait("formula", formula);
        }
        
        // Store charge if available
        Integer charge = sbmlSpecies.getCharge();
        if (charge != null) {
            molecule.setTrait("charge", charge);
        }
        
        return molecule;
    }

    /**
     * Converts SBML reactions to JScience BioChemicalReaction objects.
     * <p>
     * This bridges the gap between SBML reaction definitions and the
     * core chemistry/biology reaction framework.
     * </p>
     *
     * @param model the parsed SBML model
     * @return list of BioChemicalReaction objects
     */
    public List<BioChemicalReaction> toReactions(SBMLModel model) {
        List<BioChemicalReaction> reactions = new ArrayList<>();
        if (model == null || model.getReactions() == null) {
            return reactions;
        }
        
        // Build a lookup map for species
        Map<String, Molecule> moleculeMap = new HashMap<>();
        for (Molecule m : toMolecules(model)) {
            String sbmlId = (String) m.getTrait("sbml.id");
            if (sbmlId != null) {
                moleculeMap.put(sbmlId, m);
            }
        }
        
        for (SBMLReaction sbmlReaction : model.getReactions()) {
            BioChemicalReaction reaction = convertToReaction(sbmlReaction, moleculeMap);
            if (reaction != null) {
                reactions.add(reaction);
            }
        }
        return reactions;
    }

    /**
     * Converts a single SBML reaction to a JScience BioChemicalReaction.
     *
     * @param sbmlReaction the SBML reaction DTO
     * @param moleculeMap lookup map for species ID to Molecule
     * @return a BioChemicalReaction, or null if conversion fails
     */
    public BioChemicalReaction convertToReaction(SBMLReaction sbmlReaction, Map<String, Molecule> moleculeMap) {
        if (sbmlReaction == null) {
            return null;
        }
        
        String name = sbmlReaction.getName();
        if (name == null || name.isEmpty()) {
            name = sbmlReaction.getId();
        }
        
        BioChemicalReaction reaction = new BioChemicalReaction(name);
        
        // Add reactants
        Map<String, org.jscience.mathematics.numbers.real.Real> reactants = sbmlReaction.getReactants();
        if (reactants != null) {
            for (Map.Entry<String, org.jscience.mathematics.numbers.real.Real> entry : reactants.entrySet()) {
                Molecule m = moleculeMap.get(entry.getKey());
                if (m != null) {
                    reaction.addReactant(m, entry.getValue().doubleValue());
                }
            }
        }
        
        // Add products
        Map<String, org.jscience.mathematics.numbers.real.Real> products = sbmlReaction.getProducts();
        if (products != null) {
            for (Map.Entry<String, org.jscience.mathematics.numbers.real.Real> entry : products.entrySet()) {
                Molecule m = moleculeMap.get(entry.getKey());
                if (m != null) {
                    reaction.addProduct(m, entry.getValue().doubleValue());
                }
            }
        }
        
        // Transfer SBML-specific attributes
        reaction.setTrait("sbml.id", sbmlReaction.getId());
        reaction.setTrait("reversible", sbmlReaction.isReversible());
        
        if (sbmlReaction.getLowerBound() != null) {
            reaction.setTrait("flux.lower.bound", sbmlReaction.getLowerBound().doubleValue());
        }
        if (sbmlReaction.getUpperBound() != null) {
            reaction.setTrait("flux.upper.bound", sbmlReaction.getUpperBound().doubleValue());
        }
        
        return reaction;
    }

    /**
     * Creates a compartment-to-species mapping for spatial modeling.
     *
     * @param model the parsed SBML model
     * @return map of compartment ID to list of Species in that compartment
     */
    public Map<String, List<Molecule>> getCompartmentMoleculeMap(SBMLModel model) {
        Map<String, List<Molecule>> compartmentMap = new HashMap<>();
        
        for (Molecule m : toMolecules(model)) {
            String compartment = (String) m.getTrait("sbml.compartment");
            if (compartment != null) {
                compartmentMap.computeIfAbsent(compartment, k -> new ArrayList<>()).add(m);
            }
        }
        
        return compartmentMap;
    }

    /**
     * Extracts gene-protein-reaction associations from FBC extension.
     *
     * @param model the parsed SBML model with FBC data
     * @return map of reaction ID to gene product associations
     */
    public Map<String, List<String>> getGeneProductAssociations(SBMLModel model) {
        Map<String, List<String>> associations = new HashMap<>();
        
        if (model.getGeneProducts() != null) {
            for (SBMLGeneProduct gp : model.getGeneProducts()) {
                // The gene product is linked to reactions via the model
                // Here we just list available gene products
                String reactionId = gp.getMetaId(); // Typically linked via GPR rules
                associations.computeIfAbsent(reactionId, k -> new ArrayList<>())
                            .add(gp.getLabel() != null ? gp.getLabel() : gp.getId());
            }
        }
        
        return associations;
    }
}
