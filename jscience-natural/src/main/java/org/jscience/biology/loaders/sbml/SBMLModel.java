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

import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.mathematics.numbers.real.Real;

import java.util.*;

import org.jscience.util.UniversalDataModel;

/**
 * Represents an SBML model for systems biology.
 * <p>
 * Contains compartments, species (metabolites), reactions, and parameters
 * from an SBML file. Provides methods to construct stoichiometry matrices
 * for flux balance analysis.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SBMLModel implements UniversalDataModel {


    private String id;
    private String name;
    private int level;
    private int version;
    private String notes;
    
    private final List<SBMLCompartment> compartments = new ArrayList<>();
    private final List<SBMLSpecies> species = new ArrayList<>();
    private final List<SBMLReaction> reactions = new ArrayList<>();
    private final List<SBMLParameter> parameters = new ArrayList<>();
    private final List<SBMLGeneProduct> geneProducts = new ArrayList<>();
    
    // Index maps for quick lookup
    private final Map<String, SBMLSpecies> speciesById = new HashMap<>();
    private final Map<String, SBMLReaction> reactionsById = new HashMap<>();

    public SBMLModel() {
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Compartments
    public List<SBMLCompartment> getCompartments() {
        return Collections.unmodifiableList(compartments);
    }

    public void addCompartment(SBMLCompartment compartment) {
        if (compartment != null) {
            compartments.add(compartment);
        }
    }

    // Species
    public List<SBMLSpecies> getSpecies() {
        return Collections.unmodifiableList(species);
    }

    public void addSpecies(SBMLSpecies sp) {
        if (sp != null) {
            species.add(sp);
            if (sp.getId() != null) {
                speciesById.put(sp.getId(), sp);
            }
        }
    }

    public SBMLSpecies getSpeciesById(String id) {
        return speciesById.get(id);
    }

    public int getSpeciesCount() {
        return species.size();
    }

    // Reactions
    public List<SBMLReaction> getReactions() {
        return Collections.unmodifiableList(reactions);
    }

    public void addReaction(SBMLReaction reaction) {
        if (reaction != null) {
            reactions.add(reaction);
            if (reaction.getId() != null) {
                reactionsById.put(reaction.getId(), reaction);
            }
        }
    }

    public SBMLReaction getReactionById(String id) {
        return reactionsById.get(id);
    }

    public int getReactionCount() {
        return reactions.size();
    }

    // Parameters
    public List<SBMLParameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public void addParameter(SBMLParameter parameter) {
        if (parameter != null) {
            parameters.add(parameter);
        }
    }

    // Gene Products (FBC extension)
    public List<SBMLGeneProduct> getGeneProducts() {
        return Collections.unmodifiableList(geneProducts);
    }

    public void addGeneProduct(SBMLGeneProduct geneProduct) {
        if (geneProduct != null) {
            geneProducts.add(geneProduct);
        }
    }

    /**
     * Returns the list of internal metabolites (non-boundary species).
     */
    public List<SBMLSpecies> getInternalMetabolites() {
        List<SBMLSpecies> internal = new ArrayList<>();
        for (SBMLSpecies sp : species) {
            if (!sp.isBoundaryCondition()) {
                internal.add(sp);
            }
        }
        return internal;
    }

    /**
     * Returns the list of boundary metabolites.
     */
    public List<SBMLSpecies> getBoundaryMetabolites() {
        List<SBMLSpecies> boundary = new ArrayList<>();
        for (SBMLSpecies sp : species) {
            if (sp.isBoundaryCondition()) {
                boundary.add(sp);
            }
        }
        return boundary;
    }

    /**
     * Constructs the stoichiometry matrix S where S[i][j] is the 
     * stoichiometric coefficient of species i in reaction j.
     * <p>
     * Reactants have negative coefficients, products have positive.
     * Only internal (non-boundary) species are included.
     * </p>
     *
     * @return the stoichiometry matrix as a RealDoubleMatrix
     */
    public RealDoubleMatrix getStoichiometryMatrix() {
        List<SBMLSpecies> internalSpecies = getInternalMetabolites();
        int numSpecies = internalSpecies.size();
        int numReactions = reactions.size();
        
        // Build species index map
        Map<String, Integer> speciesIndex = new HashMap<>();
        for (int i = 0; i < internalSpecies.size(); i++) {
            speciesIndex.put(internalSpecies.get(i).getId(), i);
        }
        
        // Build stoichiometry matrix
        double[][] matrix = new double[numSpecies][numReactions];
        
        for (int j = 0; j < numReactions; j++) {
            SBMLReaction reaction = reactions.get(j);
            
            // Reactants (negative stoichiometry)
            for (Map.Entry<String, Real> entry : reaction.getReactants().entrySet()) {
                Integer i = speciesIndex.get(entry.getKey());
                if (i != null) {
                    matrix[i][j] -= entry.getValue().doubleValue();
                }
            }
            
            // Products (positive stoichiometry)
            for (Map.Entry<String, Real> entry : reaction.getProducts().entrySet()) {
                Integer i = speciesIndex.get(entry.getKey());
                if (i != null) {
                    matrix[i][j] += entry.getValue().doubleValue();
                }
            }
        }
        
        return RealDoubleMatrix.of(matrix);
    }

    /**
     * Returns species IDs in the order used by the stoichiometry matrix.
     */
    public List<String> getMetaboliteOrder() {
        List<String> order = new ArrayList<>();
        for (SBMLSpecies sp : getInternalMetabolites()) {
            order.add(sp.getId());
        }
        return order;
    }

    /**
     * Returns reaction IDs in the order used by the stoichiometry matrix.
     */
    public List<String> getReactionOrder() {
        List<String> order = new ArrayList<>();
        for (SBMLReaction rxn : reactions) {
            order.add(rxn.getId());
        }
        return order;
    }

    @Override
    public String toString() {
        return "SBMLModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", version=" + version +
                ", compartments=" + compartments.size() +
                ", species=" + species.size() +
                ", reactions=" + reactions.size() +
                '}';
    }

    @Override
    public String getModelType() {
        return "SYSTEMS_BIOLOGY_SBML";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("id", id);
        meta.put("name", name);
        meta.put("sbml_level", level);
        meta.put("sbml_version", version);
        meta.put("notes", notes);
        return meta;
    }

    @Override
    public Map<String, org.jscience.measure.Quantity<?>> getQuantities() {
        Map<String, org.jscience.measure.Quantity<?>> q = new HashMap<>();
        q.put("species_count", org.jscience.measure.Quantities.create(species.size(), org.jscience.measure.Units.ONE));
        q.put("reaction_count", org.jscience.measure.Quantities.create(reactions.size(), org.jscience.measure.Units.ONE));
        q.put("compartment_count", org.jscience.measure.Quantities.create(compartments.size(), org.jscience.measure.Units.ONE));
        return q;
    }
}

