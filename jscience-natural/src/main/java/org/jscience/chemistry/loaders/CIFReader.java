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

package org.jscience.chemistry.loaders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads Crystal Structures from CIF (Crystallographic Information File) format.
 * <p>
 * Supports basic parsing of unit cell parameters and atom sites.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CIFReader {

    public static String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.chemistry", "Chemistry");
    }

    public static String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.cif.desc", "Crystal Structure Reader (CIF).");
    }

    public static class CrystalStructure {
        public String chemicalFormula;
        public double a, b, c; // Cell lengths (Angstrom)
        public double alpha, beta, gamma; // Cell angles (Degrees)
        public List<AtomSite> atoms = new ArrayList<>();
    }

    public static class AtomSite {
        public String label;
        public String symbol;
        public double x, y, z; // Fractional coordinates

        public AtomSite(String label, String symbol, double x, double y, double z) {
            this.label = label;
            this.symbol = symbol;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static CrystalStructure load(InputStream is) {
        CrystalStructure structure = new CrystalStructure();
        if (is == null)
            return structure;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean inAtomLoop = false;
            List<String> atomHeaders = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                if (line.startsWith("_chemical_formula_sum")) {
                    structure.chemicalFormula = parseValue(line);
                } else if (line.startsWith("_cell_length_a")) {
                    structure.a = parseDouble(line);
                } else if (line.startsWith("_cell_length_b")) {
                    structure.b = parseDouble(line);
                } else if (line.startsWith("_cell_length_c")) {
                    structure.c = parseDouble(line);
                } else if (line.startsWith("_cell_angle_alpha")) {
                    structure.alpha = parseDouble(line);
                } else if (line.startsWith("_cell_angle_beta")) {
                    structure.beta = parseDouble(line);
                } else if (line.startsWith("_cell_angle_gamma")) {
                    structure.gamma = parseDouble(line);
                } else if (line.startsWith("loop_")) {
                    inAtomLoop = false; // Reset to check next lines
                    atomHeaders.clear();
                } else if (line.startsWith("_atom_site_")) {
                    inAtomLoop = true;
                    atomHeaders.add(line);
                } else if (inAtomLoop) {
                    // Data line corresponding to loop headers
                    // Assuming space delimited
                    String[] parts = line.split("\\s+");
                    if (parts.length >= atomHeaders.size()) {
                        String label = "Unk";
                        String symbol = "X";
                        double x = 0, y = 0, z = 0;

                        // Very basic mapping based on standard order assumptions or simple search
                        // For robustness, we should map headers to indices.
                        // Simplified implementation:
                        for (int i = 0; i < Math.min(parts.length, atomHeaders.size()); i++) {
                            String header = atomHeaders.get(i);
                            if (header.contains("label"))
                                label = parts[i];
                            if (header.contains("type_symbol"))
                                symbol = parts[i];
                            if (header.contains("fract_x"))
                                x = cleanDouble(parts[i]);
                            if (header.contains("fract_y"))
                                y = cleanDouble(parts[i]);
                            if (header.contains("fract_z"))
                                z = cleanDouble(parts[i]);
                        }
                        structure.atoms.add(new AtomSite(label, symbol, x, y, z));
                    } else {
                        // End of loop often
                        inAtomLoop = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return structure;
    }

    private static String parseValue(String line) {
        String[] parts = line.split("\\s+", 2);
        if (parts.length > 1) {
            return parts[1].replace("'", "").replace("\"", "");
        }
        return "";
    }

    private static double parseDouble(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length > 1) {
            return cleanDouble(parts[1]);
        }
        return 0.0;
    }

    // Removes uncertainties like 1.234(5)
    private static double cleanDouble(String val) {
        int paren = val.indexOf('(');
        if (paren > 0) {
            val = val.substring(0, paren);
        }
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}



