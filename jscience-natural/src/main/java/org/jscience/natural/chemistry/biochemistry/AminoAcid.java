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

package org.jscience.natural.chemistry.biochemistry;

import org.jscience.natural.chemistry.Molecule;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard amino acid data.
 * Extends Molecule to allow chemical operations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class AminoAcid extends Molecule {

    private static final long serialVersionUID = 2L;

    // Static registry
    private static final List<AminoAcid> VALUES = new ArrayList<>();
    private static final java.util.Map<String, AminoAcid> CACHE = new java.util.HashMap<>();

    static {
        loadData();
    }

    // Standard Amino Acids
    public static final AminoAcid GLYCINE = getStandard("Glycine");
    public static final AminoAcid ALANINE = getStandard("Alanine");
    public static final AminoAcid VALINE = getStandard("Valine");
    public static final AminoAcid LEUCINE = getStandard("Leucine");
    public static final AminoAcid ISOLEUCINE = getStandard("Isoleucine");
    public static final AminoAcid PROLINE = getStandard("Proline");
    public static final AminoAcid METHIONINE = getStandard("Methionine");

    public static final AminoAcid PHENYLALANINE = getStandard("Phenylalanine");
    public static final AminoAcid TYROSINE = getStandard("Tyrosine");
    public static final AminoAcid TRYPTOPHAN = getStandard("Tryptophan");

    public static final AminoAcid SERINE = getStandard("Serine");
    public static final AminoAcid THREONINE = getStandard("Threonine");
    public static final AminoAcid CYSTEINE = getStandard("Cysteine");
    public static final AminoAcid ASPARAGINE = getStandard("Asparagine");
    public static final AminoAcid GLUTAMINE = getStandard("Glutamine");

    public static final AminoAcid LYSINE = getStandard("Lysine");
    public static final AminoAcid ARGININE = getStandard("Arginine");
    public static final AminoAcid HISTIDINE = getStandard("Histidine");

    public static final AminoAcid ASPARTATE = getStandard("Aspartate");
    public static final AminoAcid GLUTAMATE = getStandard("Glutamate");

    private static AminoAcid getStandard(String name) {
        if (CACHE.isEmpty()) {
            if (VALUES.isEmpty())
                loadData();
        }
        return CACHE.get(name);
    }

    private static void loadData() {
        if (!VALUES.isEmpty())
            return;
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.io.InputStream is = AminoAcid.class
                    .getResourceAsStream("/org/jscience/chemistry/biochemistry/amino_acids.json");
            if (is == null) {
                java.util.logging.Logger.getLogger("AminoAcid").severe("amino_acids.json not found!");
                return;
            }
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(is);
            if (root.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode node : root) {
                    new AminoAcid(
                            node.get("name").asText(),
                            node.get("three").asText(),
                            node.get("one").asText(),
                            Real.of(node.get("mass").asDouble()),
                            Real.of(node.get("pI").asDouble()),
                            node.get("type").asText());
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Attribute
    private final String threeLetterCode;
    @Attribute
    private final String oneLetterCode;
    @Attribute
    private final Real explicitMolecularWeight; // g/mol
    @Attribute
    private final Real pI; // Isoelectric point
    @Attribute
    private final String classification;

    private AminoAcid(String name, String three, String one, Real mw, Real pI, String classification) {
        super(name);
        this.threeLetterCode = three;
        this.oneLetterCode = one;
        this.explicitMolecularWeight = mw;
        this.pI = pI;
        this.classification = classification;
        VALUES.add(this);
        CACHE.put(name, this);
    }

    public String getThreeLetterCode() {
        return threeLetterCode;
    }

    public String getOneLetterCode() {
        return oneLetterCode;
    }

    /**
     * Returns the molecular weight.
     * Overrides Molecule implementation to return the standard stored weight
     * since we aren't populating atoms yet.
     */
    @Override
    public org.jscience.core.measure.Quantity<org.jscience.core.measure.quantity.Mass> getMolecularWeight() {
        // 1 u = 1.66053906660e-27 kg
        Real kgPerAmu = Real.of("1.66053906660e-27");
        Real massInKg = explicitMolecularWeight.multiply(kgPerAmu);
        return org.jscience.core.measure.Quantities.create(massInKg, org.jscience.core.measure.Units.KILOGRAM);
    }

    /**
     * Gets the molar mass in g/mol.
     */
    public Real getMolarMass() {
        return explicitMolecularWeight;
    }

    public Real getIsoelectricPoint() {
        return pI;
    }

    public String getClassification() {
        return classification;
    }

    /**
     * Mimics Enum.values().
     */
    public static AminoAcid[] values() {
        if (VALUES.isEmpty())
            loadData();
        return VALUES.toArray(new AminoAcid[0]);
    }

    /**
     * Mimics Enum.valueOf().
     */
    public static AminoAcid valueOf(String name) {
        if (VALUES.isEmpty())
            loadData();
        for (AminoAcid aa : VALUES) {
            if (aa.getName().equalsIgnoreCase(name))
                return aa;
        }
        throw new IllegalArgumentException("No amino acid constant " + name);
    }

    /**
     * Looks up amino acid by one-letter code.
     */
    public static AminoAcid fromCode(char code) {
        String s = String.valueOf(code).toUpperCase();
        for (AminoAcid aa : VALUES) {
            if (aa.oneLetterCode.equals(s))
                return aa;
        }
        return null;
    }

    /**
     * Calculates molecular weight of a peptide sequence.
     * Subtracts water for peptide bond formation.
     * Returns value in g/mol.
     */
    public static Real peptideWeight(String sequence) {
        Real weight = Real.ZERO;
        for (char c : sequence.toCharArray()) {
            AminoAcid aa = fromCode(c);
            if (aa != null) {
                weight = weight.add(aa.explicitMolecularWeight);
            }
        }
        // Subtract (n-1) * 18.01528 for water loss in peptide bonds
        int bonds = Math.max(0, sequence.length() - 1);
        weight = weight.subtract(Real.of(18.01528).multiply(Real.of(bonds)));
        return weight;
    }

    /**
     * Checks if amino acid is hydrophobic.
     */
    public boolean isHydrophobic() {
        return classification.equals("nonpolar") || classification.equals("aromatic");
    }

    /**
     * Checks if amino acid is charged at pH 7.
     */
    public boolean isCharged() {
        return classification.equals("basic") || classification.equals("acidic");
    }
}


