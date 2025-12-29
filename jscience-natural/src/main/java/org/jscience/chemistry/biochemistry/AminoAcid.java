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

package org.jscience.chemistry.biochemistry;

import org.jscience.chemistry.Molecule;

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
public class AminoAcid extends Molecule {

    // Static registry
    private static final List<AminoAcid> VALUES = new ArrayList<>();

    // Constants will be initialized after load, but we can't assign to static final
    // fields if we want them to be driven by JSON completely.
    // However, to maintain API compatibility and convenience, we will attempt to
    // find them in the loaded values.
    // Hack: We can't really set "static final" fields from a loader method unless
    // we do it in a static block that does the work.

    // Standard Amino Acids (Exposed as public static final for ABI, but effectively
    // lookups? No, they must be objects)
    // We will initialize them by calling a private helper in the declaration.

    private static final java.util.Map<String, AminoAcid> CACHE = new java.util.HashMap<>();

    static {
        loadData();
    }

    // We have to remove the 'final' from these if we want to set them from JSON, OR
    // we keep them as is
    // but the constructor puts them in the registry.
    // BUT the user wanted EXTERNALIZATION. This means the properties (mass, pI)
    // should come from JSON.
    // So we CANNOT instantiate them with hardcoded values here.

    // Solution:
    // 1. Load JSON into a Map<String, AminoAcidDTO> or similar.
    // 2. Initialize the constants by querying the map.
    // 3. Since 'static final' fields are initialized at class load time, we can run
    // the loader in a static block
    // BEFORE the fields are initialized? No, fields are initialized in textual
    // order?
    // Actually, if we call a method to init the field, that method can ensure data
    // is loaded.

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
            // Should have been loaded by static block top, but safety check or re-init?
            // "static { loadData(); }" runs before fields if it is lexicographically
            // before?
            // OR we just ensure loadData() is called idemptotently.
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
                            node.get("mass").asDouble(),
                            node.get("pI").asDouble(),
                            node.get("type").asText());
                }
            }
            is.close();
        } catch (Exception e) {
            // Fallback for development if file missing/broken?
            e.printStackTrace();
        }
    }

    private final String threeLetterCode;
    private final String oneLetterCode;
    private final double explicitMolecularWeight; // g/mol
    private final double pI; // Isoelectric point
    private final String classification;

    private AminoAcid(String name, String three, String one, double mw, double pI, String classification) {
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
    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Mass> getMolecularWeight() {
        // 1 u = 1.66053906660e-27 kg
        double massInKg = explicitMolecularWeight * 1.66053906660e-27;
        return org.jscience.measure.Quantities.create(massInKg, org.jscience.measure.Units.KILOGRAM);
    }

    /**
     * Gets the molar mass in g/mol.
     */
    public double getMolarMass() {
        return explicitMolecularWeight;
    }

    public double getIsoelectricPoint() {
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
        throw new IllegalArgumentException("No enum constant " + name);
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
    public static double peptideWeight(String sequence) {
        double weight = 0;
        for (char c : sequence.toCharArray()) {
            AminoAcid aa = fromCode(c);
            if (aa != null) {
                weight += aa.explicitMolecularWeight;
            }
        }
        // Subtract (n-1) * 18.015 for water loss in peptide bonds
        int bonds = Math.max(0, sequence.length() - 1);
        weight -= bonds * 18.015;
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
