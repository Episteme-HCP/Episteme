package org.jscience.biology.cell;

/**
 * Organelles within the cell.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public enum Organelle {

    NUCLEUS("Contains genetic material (DNA)"),
    MITOCHONDRIA("Powerhouse, generates ATP via respiration"),
    RIBOSOME("Site of protein synthesis"),
    ENDOPLASMIC_RETICULUM_ROUGH("Protein synthesis and folding"),
    ENDOPLASMIC_RETICULUM_SMOOTH("Lipid synthesis, detoxification"),
    GOLGI_APPARATUS("Packaging and secretion of proteins"),
    LYSOSOME("Digestion and waste removal"),
    VACUOLE("Storage, turgor pressure (plants)"),
    CHLOROPLAST("Photosynthesis (plants)"),
    CYTOSKELETON("Structural support and transport"),
    CELL_MEMBRANE("Separates cell from environment");

    private final String function;

    Organelle(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }
}
