/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.biology.loaders.sbml;

import org.episteme.core.mathematics.numbers.real.Real;

import java.util.*;

/**
 * Represents a reaction in an SBML model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SBMLReaction {

    private String id;
    private String name;
    private boolean reversible = true;
    private boolean fast = false;
    private String kineticLawFormula;
    
    private final Map<String, Real> reactants = new LinkedHashMap<>();
    private final Map<String, Real> products = new LinkedHashMap<>();
    private final List<String> modifiers = new ArrayList<>();
    private final Map<String, Real> localParameters = new HashMap<>();
    
    // FBC extension data
    private Real lowerFluxBound;
    private Real upperFluxBound;
    private String geneProductAssociation;

    public SBMLReaction() {
    }

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

    public boolean isReversible() {
        return reversible;
    }

    public void setReversible(boolean reversible) {
        this.reversible = reversible;
    }

    public boolean isFast() {
        return fast;
    }

    public void setFast(boolean fast) {
        this.fast = fast;
    }

    public String getKineticLawFormula() {
        return kineticLawFormula;
    }

    public void setKineticLawFormula(String kineticLawFormula) {
        this.kineticLawFormula = kineticLawFormula;
    }

    // Reactants
    public Map<String, Real> getReactants() {
        return Collections.unmodifiableMap(reactants);
    }

    public void addReactant(String speciesId, Real stoichiometry) {
        reactants.put(speciesId, stoichiometry);
    }

    // Products
    public Map<String, Real> getProducts() {
        return Collections.unmodifiableMap(products);
    }

    public void addProduct(String speciesId, Real stoichiometry) {
        products.put(speciesId, stoichiometry);
    }

    // Modifiers
    public List<String> getModifiers() {
        return Collections.unmodifiableList(modifiers);
    }

    public void addModifier(String speciesId) {
        modifiers.add(speciesId);
    }

    // Local parameters
    public Map<String, Real> getLocalParameters() {
        return Collections.unmodifiableMap(localParameters);
    }

    public void addLocalParameter(String parameterId, Real value) {
        localParameters.put(parameterId, value);
    }

    // FBC extension
    public Real getLowerBound() {
        return lowerFluxBound;
    }

    public void setLowerBound(Real lowerFluxBound) {
        this.lowerFluxBound = lowerFluxBound;
    }

    public Real getUpperBound() {
        return upperFluxBound;
    }

    public void setUpperBound(Real upperFluxBound) {
        this.upperFluxBound = upperFluxBound;
    }

    public String getGeneProductAssociation() {
        return geneProductAssociation;
    }

    public void setGeneProductAssociation(String geneProductAssociation) {
        this.geneProductAssociation = geneProductAssociation;
    }

    /**
     * Returns a human-readable reaction equation string.
     */
    public String getEquationString() {
        StringBuilder sb = new StringBuilder();
        
        // Reactants
        boolean first = true;
        for (Map.Entry<String, Real> entry : reactants.entrySet()) {
            if (!first) sb.append(" + ");
            if (entry.getValue().doubleValue() != 1.0) {
                sb.append(entry.getValue().doubleValue()).append(" ");
            }
            sb.append(entry.getKey());
            first = false;
        }
        
        // Arrow
        sb.append(reversible ? " <=> " : " -> ");
        
        // Products
        first = true;
        for (Map.Entry<String, Real> entry : products.entrySet()) {
            if (!first) sb.append(" + ");
            if (entry.getValue().doubleValue() != 1.0) {
                sb.append(entry.getValue().doubleValue()).append(" ");
            }
            sb.append(entry.getKey());
            first = false;
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return "SBMLReaction{id='" + id + "', equation=" + getEquationString() + "}";
    }
}

