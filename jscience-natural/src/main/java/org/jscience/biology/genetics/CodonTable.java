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

package org.jscience.biology.genetics;

import org.jscience.chemistry.biochemistry.AminoAcid;

import java.util.HashMap;
import java.util.Map;

/**
 * The standard genetic code for translating RNA codons to Amino Acids.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public enum CodonTable {

    STANDARD;

    private final Map<String, AminoAcid> codonMap;
    private final Map<String, Character> codonCharMap;

    CodonTable() {
        codonMap = new HashMap<>();
        codonCharMap = new HashMap<>();
        initializeCode();
    }

    private void initializeCode() {
        // Load from JSON
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.io.InputStream is = CodonTable.class
                    .getResourceAsStream("/org/jscience/biology/codons.json");
            if (is == null) {
                java.util.logging.Logger.getLogger("CodonTable").severe("codons.json not found!");
                return;
            }
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(is);
            com.fasterxml.jackson.databind.JsonNode codonsNode = root.get("codons");

            java.util.Iterator<Map.Entry<String, com.fasterxml.jackson.databind.JsonNode>> fields = codonsNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, com.fasterxml.jackson.databind.JsonNode> entry = fields.next();
                String codon = entry.getKey();
                com.fasterxml.jackson.databind.JsonNode data = entry.getValue();

                if (data.has("stop") && data.get("stop").asBoolean()) {
                    register(codon, null);
                } else {
                    String aaCode = data.get("aminoAcid").asText();
                    AminoAcid aa = AminoAcid.fromCode(aaCode.charAt(0));
                    if (aa != null) {
                        register(codon, aa);
                    } else {
                        // Warning: Amino Acid not found?
                        java.util.logging.Logger.getLogger("CodonTable")
                                .warning("Amino Acid not found for code: " + aaCode);
                    }
                }
            }

            // Handle expanded codes (Selenocysteine, Pyrrolysine)
            if (root.has("expandedCodes")) {
                root.get("expandedCodes");
                // Logic to register these special cases contextually would go here
                // For now, we note them. The standard table uses the stop codon mapping.
            }

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(String codon, AminoAcid aa) {
        codonMap.put(codon, aa);
        if (aa != null) {
            // Use standard one-letter code from AminoAcid class
            char code = aa.getOneLetterCode().charAt(0);
            codonCharMap.put(codon, code);
        } else {
            codonCharMap.put(codon, '*'); // Stop
        }
    }

    /**
     * Translates a codon to an Amino Acid.
     * 
     * @param codon 3-character RNA string (e.g., "AUG")
     * @return AminoAcid or null for STOP
     */
    public AminoAcid translate(String codon) {
        return codonMap.get(codon.toUpperCase().replace('T', 'U'));
    }

    /**
     * Translates a codon to its single-letter representation.
     */
    public char translateToChar(String codon) {
        Character c = codonCharMap.get(codon.toUpperCase().replace('T', 'U'));
        return c != null ? c : 'X'; // X for unknown
    }

    /**
     * Checks if a codon is a stop codon.
     */
    public boolean isStop(String codon) {
        return !codonMap.containsKey(codon.toUpperCase().replace('T', 'U'))
                && codonCharMap.get(codon.toUpperCase().replace('T', 'U')) == '*';
    }

    /**
     * Checks if a codon is a start codon (AUG).
     */
    public boolean isStart(String codon) {
        return codon.toUpperCase().replace('T', 'U').equals("AUG");
    }
}
