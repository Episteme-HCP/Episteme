/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.chemistry.loaders;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;

/**
 * A data transfer object for chemical compound information from external APIs.
 * <p>
 * This class captures common properties returned by services like PubChem:
 * <ul>
 * <li>PubChem CID (compound identifier)</li>
 * <li>Molecular formula</li>
 * <li>Molecular weight</li>
 * <li>IUPAC name</li>
 * <li>InChIKey (structural identifier)</li>
 * <li>Canonical SMILES</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CompoundInfo {

    private final long cid;
    private final String name;
    private final String molecularFormula;
    private final double molecularWeight;
    private final String iupacName;
    private final String inchiKey;
    private final String canonicalSmiles;

    private CompoundInfo(Builder builder) {
        this.cid = builder.cid;
        this.name = builder.name;
        this.molecularFormula = builder.molecularFormula;
        this.molecularWeight = builder.molecularWeight;
        this.iupacName = builder.iupacName;
        this.inchiKey = builder.inchiKey;
        this.canonicalSmiles = builder.canonicalSmiles;
    }

    /**
     * Returns the PubChem Compound ID.
     * 
     * @return CID
     */
    public long getCid() {
        return cid;
    }

    /**
     * Returns the common name of the compound.
     * 
     * @return compound name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the molecular formula (e.g., "C9H8O4" for Aspirin).
     * 
     * @return molecular formula
     */
    public String getMolecularFormula() {
        return molecularFormula;
    }

    /**
     * Returns the molecular weight in g/mol.
     * 
     * @return molecular weight
     */
    public double getMolecularWeight() {
        return molecularWeight;
    }

    /**
     * Returns the molecular weight as a type-safe Quantity.
     * 
     * @return molecular weight quantity (in g/mol, approximated as GRAM)
     */
    public Quantity<Mass> getMolecularWeightQuantity() {
        // Molar mass in g/mol - using GRAM as approximation for g/mol
        return Quantities.create(molecularWeight / 1000.0, Units.KILOGRAM);
    }

    /**
     * Returns the IUPAC systematic name.
     * 
     * @return IUPAC name
     */
    public String getIupacName() {
        return iupacName;
    }

    /**
     * Returns the InChIKey structural identifier.
     * 
     * @return InChIKey
     */
    public String getInchiKey() {
        return inchiKey;
    }

    /**
     * Returns the canonical SMILES representation.
     * 
     * @return SMILES string
     */
    public String getCanonicalSmiles() {
        return canonicalSmiles;
    }

    @Override
    public String toString() {
        return String.format("CompoundInfo{cid=%d, name='%s', formula='%s', MW=%.2f g/mol}",
                cid, name, molecularFormula, molecularWeight);
    }

    /**
     * Builder for CompoundInfo instances.
     */
    public static class Builder {
        private long cid;
        private String name = "";
        private String molecularFormula = "";
        private double molecularWeight;
        private String iupacName = "";
        private String inchiKey = "";
        private String canonicalSmiles = "";

        public Builder cid(long cid) {
            this.cid = cid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder molecularFormula(String formula) {
            this.molecularFormula = formula;
            return this;
        }

        public Builder molecularWeight(double weight) {
            this.molecularWeight = weight;
            return this;
        }

        public Builder iupacName(String iupacName) {
            this.iupacName = iupacName;
            return this;
        }

        public Builder inchiKey(String inchiKey) {
            this.inchiKey = inchiKey;
            return this;
        }

        public Builder canonicalSmiles(String smiles) {
            this.canonicalSmiles = smiles;
            return this;
        }

        public CompoundInfo build() {
            return new CompoundInfo(this);
        }
    }
}
