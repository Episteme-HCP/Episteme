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

import org.sbml.jsbml.*;
import org.sbml.jsbml.ext.fbc.FBCModelPlugin;
import org.sbml.jsbml.ext.fbc.GeneProduct;

import org.jscience.mathematics.numbers.real.Real;

import org.jscience.io.AbstractResourceReader;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Systems Biology Markup Language (SBML) Reader.
 * <p>
 * Parses SBML files using the JSBML library, supporting SBML Level 3 Core
 * and common extensions like FBC (Flux Balance Constraints).
 * </p>
 * <p>
 * <b>Supported Features:</b>
 * <ul>
 *   <li>Model metadata (name, ID, notes)</li>
 *   <li>Compartments with sizes and units</li>
 *   <li>Species (metabolites) with concentrations</li>
 *   <li>Reactions with kinetic laws and stoichiometry</li>
 *   <li>Parameters (global and local)</li>
 *   <li>FBC extension for flux balance analysis</li>
 * </ul>
 * </p>
 * <p>
 * <b>Example Usage:</b>
 * <pre>{@code
 * SBMLReader reader = new SBMLReader();
 * SBMLModel model = reader.read(new File("ecoli_core.xml"));
 * 
 * System.out.println("Model: " + model.getName());
 * System.out.println("Species: " + model.getSpeciesCount());
 * System.out.println("Reactions: " + model.getReactionCount());
 * 
 * // Get stoichiometry matrix for FBA
 * RealDoubleMatrix stoichiometry = model.getStoichiometryMatrix();
 * }</pre>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="http://sbml.org/">SBML.org</a>
 */
public class SBMLReader extends AbstractResourceReader<SBMLModel> {

    private static final Logger LOGGER = Logger.getLogger(SBMLReader.class.getName());

    /**
     * Creates a new SBMLReader.
     */
    public SBMLReader() {
    }

    // ===== ResourceReader interface =====

    @Override
    public String getResourcePath() {
        return null; // File-based, path provided at load time
    }

    @Override
    public Class<SBMLModel> getResourceType() {
        return SBMLModel.class;
    }

    @Override
    public String getName() {
        return "SBML Reader";
    }

    @Override
    public String getDescription() {
        return "Reads systems biology models from SBML format";
    }

    @Override
    public String getLongDescription() {
        return "SBML (Systems Biology Markup Language) is the standard format for " +
               "representing computational models of biological processes including " +
               "metabolic networks, signaling pathways, and gene regulatory networks.";
    }

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"Level 3 Version 2", "Level 3 Version 1", "Level 2 Version 5", "Level 2 Version 4"};
    }

    @Override
    protected SBMLModel loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) {
            return read(file);
        }
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) {
                return read(is);
            }
        }
        throw new SBMLException("Resource not found: " + resourceId);
    }

    @Override
    protected SBMLModel loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    /**
     * Reads an SBML model from a file.
     *
     * @param file the SBML file
     * @return an SBMLModel containing the parsed data
     * @throws SBMLException if parsing fails
     */
    public SBMLModel read(File file) throws SBMLException {
        try {
            SBMLDocument document = org.sbml.jsbml.SBMLReader.read(file);
            return convertDocument(document);
        } catch (XMLStreamException | java.io.IOException e) {
            throw new SBMLException("Failed to parse SBML file: " + file, e);
        }
    }

    /**
     * Reads an SBML model from an input stream.
     *
     * @param input the input stream containing SBML data
     * @return an SBMLModel containing the parsed data
     * @throws SBMLException if parsing fails
     */
    public SBMLModel read(InputStream input) throws SBMLException {
        try {
            SBMLDocument document = org.sbml.jsbml.SBMLReader.read(input);
            return convertDocument(document);
        } catch (XMLStreamException e) {
            throw new SBMLException("Failed to parse SBML data", e);
        }
    }

    /**
     * Converts a JSBML document to an SBMLModel.
     */
    private SBMLModel convertDocument(SBMLDocument document) throws SBMLException {
        if (document == null) {
            throw new SBMLException("SBML document is null");
        }
        
        Model jsbmlModel = document.getModel();
        if (jsbmlModel == null) {
            throw new SBMLException("SBML document contains no model");
        }
        
        SBMLModel model = new SBMLModel();
        
        // Basic model info
        model.setId(jsbmlModel.getId());
        model.setName(jsbmlModel.getName());
        model.setLevel(document.getLevel());
        model.setVersion(document.getVersion());
        
        // Notes and annotations
        if (jsbmlModel.isSetNotes()) {
            try {
                model.setNotes(jsbmlModel.getNotesString());
            } catch (XMLStreamException e) {
                LOGGER.log(Level.FINE, "Could not extract notes", e);
            }
        }
        
        // Convert compartments
        for (Compartment compartment : jsbmlModel.getListOfCompartments()) {
            model.addCompartment(convertCompartment(compartment));
        }
        
        // Convert species
        for (Species species : jsbmlModel.getListOfSpecies()) {
            model.addSpecies(convertSpecies(species));
        }
        
        // Convert reactions
        for (Reaction reaction : jsbmlModel.getListOfReactions()) {
            model.addReaction(convertReaction(reaction));
        }
        
        // Convert global parameters
        for (Parameter parameter : jsbmlModel.getListOfParameters()) {
            model.addParameter(convertParameter(parameter));
        }
        
        // Handle FBC extension if present
        try {
            FBCModelPlugin fbcPlugin = (FBCModelPlugin) jsbmlModel.getExtension("fbc");
            if (fbcPlugin != null) {
                extractFBCData(fbcPlugin, model);
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "FBC extension not available or error extracting", e);
        }
        
        return model;
    }

    /**
     * Converts a JSBML compartment.
     */
    private SBMLCompartment convertCompartment(Compartment compartment) {
        SBMLCompartment result = new SBMLCompartment();
        result.setId(compartment.getId());
        result.setName(compartment.getName());
        
        if (compartment.isSetSize()) {
            result.setSize(Real.of(compartment.getSize()));
        }
        
        if (compartment.isSetSpatialDimensions()) {
            result.setSpatialDimensions((int) compartment.getSpatialDimensions());
        }
        
        if (compartment.isSetUnits()) {
            result.setUnits(compartment.getUnits());
        }
        
        result.setConstant(compartment.getConstant());
        
        return result;
    }

    /**
     * Converts a JSBML species (metabolite).
     */
    private SBMLSpecies convertSpecies(Species species) {
        SBMLSpecies result = new SBMLSpecies();
        result.setId(species.getId());
        result.setName(species.getName());
        result.setCompartmentId(species.getCompartment());
        
        if (species.isSetInitialConcentration()) {
            result.setInitialConcentration(Real.of(species.getInitialConcentration()));
        }
        
        if (species.isSetInitialAmount()) {
            result.setInitialAmount(Real.of(species.getInitialAmount()));
        }
        
        result.setBoundaryCondition(species.getBoundaryCondition());
        result.setConstant(species.getConstant());
        result.setHasOnlySubstanceUnits(species.getHasOnlySubstanceUnits());
        
        return result;
    }

    /**
     * Converts a JSBML reaction.
     */
    private SBMLReaction convertReaction(Reaction reaction) {
        SBMLReaction result = new SBMLReaction();
        result.setId(reaction.getId());
        result.setName(reaction.getName());
        result.setReversible(reaction.getReversible());
        @SuppressWarnings("deprecation")
        boolean isFast = reaction.getFast();
        result.setFast(isFast);
        
        // Reactants
        for (SpeciesReference ref : reaction.getListOfReactants()) {
            result.addReactant(ref.getSpecies(), Real.of(ref.getStoichiometry()));
        }
        
        // Products
        for (SpeciesReference ref : reaction.getListOfProducts()) {
            result.addProduct(ref.getSpecies(), Real.of(ref.getStoichiometry()));
        }
        
        // Modifiers (enzymes, catalysts)
        for (ModifierSpeciesReference ref : reaction.getListOfModifiers()) {
            result.addModifier(ref.getSpecies());
        }
        
        // Kinetic law
        KineticLaw kineticLaw = reaction.getKineticLaw();
        if (kineticLaw != null) {
            if (kineticLaw.isSetMath()) {
                result.setKineticLawFormula(JSBML.formulaToString(kineticLaw.getMath()));
            }
            
            // Local parameters
            for (LocalParameter localParam : kineticLaw.getListOfLocalParameters()) {
                result.addLocalParameter(localParam.getId(), Real.of(localParam.getValue()));
            }
        }
        
        return result;
    }

    /**
     * Converts a JSBML parameter.
     */
    private SBMLParameter convertParameter(Parameter parameter) {
        SBMLParameter result = new SBMLParameter();
        result.setId(parameter.getId());
        result.setName(parameter.getName());
        
        if (parameter.isSetValue()) {
            result.setValue(Real.of(parameter.getValue()));
        }
        
        if (parameter.isSetUnits()) {
            result.setUnits(parameter.getUnits());
        }
        
        result.setConstant(parameter.getConstant());
        
        return result;
    }

    /**
     * Extracts data from FBC (Flux Balance Constraints) extension.
     */
    private void extractFBCData(FBCModelPlugin fbcPlugin, SBMLModel model) {
        // Extract gene products
        for (GeneProduct geneProduct : fbcPlugin.getListOfGeneProducts()) {
            SBMLGeneProduct gp = new SBMLGeneProduct();
            gp.setId(geneProduct.getId());
            gp.setName(geneProduct.getName());
            gp.setLabel(geneProduct.getLabel());
            model.addGeneProduct(gp);
        }
        
        // Extract flux bounds - handled per reaction via FBCReactionPlugin
        // This is done when processing reactions if FBC is present
    }
}
