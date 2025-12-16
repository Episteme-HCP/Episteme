package org.jscience.biology.genetics;

import org.jscience.chemistry.biochemistry.AminoAcid;

import java.util.HashMap;
import java.util.Map;

/**
 * The standard genetic code for translating RNA codons to Amino Acids.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
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
                    .getResourceAsStream("/org/jscience/biology/genetics/codon_table.json");
            if (is == null) {
                java.util.logging.Logger.getLogger("CodonTable").severe("codon_table.json not found!");
                return;
            }
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(is);

            java.util.Iterator<Map.Entry<String, com.fasterxml.jackson.databind.JsonNode>> fields = root.fields();
            while (fields.hasNext()) {
                Map.Entry<String, com.fasterxml.jackson.databind.JsonNode> entry = fields.next();
                String codon = entry.getKey();
                String aaOneLetter = entry.getValue().asText();

                if (aaOneLetter.equals("*")) {
                    register(codon, null);
                } else {
                    AminoAcid aa = AminoAcid.fromCode(aaOneLetter.charAt(0));
                    if (aa != null) {
                        register(codon, aa);
                    } else {
                        // Warning: Amino Acid not found?
                        java.util.logging.Logger.getLogger("CodonTable")
                                .warning("Amino Acid not found for code: " + aaOneLetter);
                    }
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to minimal if needed?
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
