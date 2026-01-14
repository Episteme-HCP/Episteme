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

package org.jscience.biology.loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.biology.Protein;
import org.jscience.biology.Protein.Chain;
import org.jscience.biology.Protein.Residue;
import org.jscience.mathematics.geometry.Vector3D;

/**
 * PDB (Protein Data Bank) file parser.
 * Builds a Protein structure from ATOM records.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PDBParser {

    /**
     * Parses PDB content into a Protein object.
     * 
     * @param pdbContent String content of the PDB file
     * @param pdbId      Identifier for the protein (optional)
     * @return Protein model
     * @throws IOException If reading fails
     */
    public static Protein parse(String pdbContent, String pdbId) throws IOException {
        Protein protein = new Protein(pdbId);
        BufferedReader reader = new BufferedReader(new StringReader(pdbContent));
        String line;

        Chain currentChain = null;
        Residue currentResidue = null;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("ATOM  ") || line.startsWith("HETATM")) {
                try {
                    // Fixed width format parsing
                    // Columns are 1-based in PDB spec, 0-based in Java strings

                    // Serial: 7-11
                    // String name: 13-16 (Atom Name)
                    String name = line.substring(12, 16).trim();

                    // AltLoc: 17
                    // String resName: 18-20 (Residue Name)
                    String resName = line.substring(17, 20).trim();

                    // ChainID: 22
                    String chainID = line.substring(21, 22);
                    if (chainID.trim().isEmpty())
                        chainID = "A"; // Default if missing

                    // ResSeq: 23-26
                    int resSeq = Integer.parseInt(line.substring(22, 26).trim());

                    // X, Y, Z coordinates
                    double x = Double.parseDouble(line.substring(30, 38).trim());
                    double y = Double.parseDouble(line.substring(38, 46).trim());
                    double z = Double.parseDouble(line.substring(46, 54).trim());

                    // Element: 77-78
                    String elementSymbol = "";
                    if (line.length() >= 78) {
                        elementSymbol = line.substring(76, 78).trim();
                    }

                    // If element symbol is empty in file, try to derive from atom name
                    if (elementSymbol.isEmpty() && !name.isEmpty()) {
                        // Heuristic: First letter of name usually, unless H- prefixed?
                        // Actually standard is first 2 chars often, but usually first letter for
                        // organic like C, N, O, S, H
                        // If Ca (Calcium), name is " CA " etc.
                        // Let's assume standard PDB where element column is populated or deduce simply.
                        elementSymbol = name.substring(0, 1);
                        if (name.length() > 1 && Character.isLowerCase(name.charAt(1))) {
                            elementSymbol = name.substring(0, 2);
                        }
                    }

                    // Lookup Element
                    Element element = PeriodicTable.bySymbol(elementSymbol);
                    if (element == null) {
                        element = PeriodicTable.getElement("Carbon"); // Fallback
                    }

                    // Create Atom
                    // Note: Coordinates in PDB are Angstroms (1e-10 m).
                    // JScience vectors usually standard units (meters)?
                    // Atom.java says "Vector3D position; // in Meters"
                    // So convert Angstrom -> Meters
                    double angstromToMeter = 1e-10;
                    Vector3D pos = new Vector3D(x * angstromToMeter, y * angstromToMeter, z * angstromToMeter);

                    Atom atom = new Atom(element, pos);

                    // Structural Hierarchy Management

                    // Check Chain
                    if (currentChain == null || !currentChain.getChainId().equals(chainID)) {
                        // Start new chain
                        // Check if we already have this chain in protein (PDB file atoms sort order?)
                        // PDB files usually group by chain.
                        Chain existing = protein.getChain(chainID);
                        if (existing != null) {
                            currentChain = existing;
                        } else {
                            currentChain = new Chain(chainID);
                            protein.addChain(currentChain);
                        }
                        currentResidue = null; // Force new residue on chain switch
                    }

                    // Check Residue
                    if (currentResidue == null || currentResidue.getSequenceNumber() != resSeq) {
                        currentResidue = new Residue(resName, resSeq);
                        currentChain.addResidue(currentResidue);
                    }

                    // Add atom to residue
                    currentResidue.addAtom(atom);

                } catch (Exception e) {
                    // Tolerant parsing: ignore defective lines
                    // e.printStackTrace();
                }
            }
        }

        return protein;
    }
}
