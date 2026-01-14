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

package org.jscience.biology.cell;

/**
 * Organelles within the cell.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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


