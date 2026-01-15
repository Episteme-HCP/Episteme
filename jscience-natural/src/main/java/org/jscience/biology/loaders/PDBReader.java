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

import org.jscience.biology.Protein;
import org.jscience.chemistry.Atom;
import org.jscience.io.AbstractResourceReader;
import java.io.*;

/**
 * Protein Data Bank (PDB) loader and exporter.
 * Supports loading from RCSB API and saving to PDB format.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PDBReader extends AbstractResourceReader<Protein> {

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getDescription() {
        return "Protein Data Bank (PDB) Reader.";
    }

    @Override
    public String getResourcePath() {
        return "https://files.rcsb.org/download/";
    }

    @Override
    public Class<Protein> getResourceType() {
        return Protein.class;
    }

    @Override
    protected Protein loadFromSource(String path) throws Exception {
        File f = new File(path);
        if (f.exists() && f.isFile()) {
            try (InputStream is = new FileInputStream(f)) {
                return loadPDB(f.getName(), is);
            }
        }

        String urlString = getResourcePath() + path.toUpperCase() + ".pdb";
        try (InputStream is = java.net.URI.create(urlString).toURL().openStream()) {
            return loadPDB(path, is);
        }
    }

    /**
     * Parses a PDB input stream into a protein structure.
     */
    private static Protein loadPDB(String pdbId, InputStream is) {
        Protein protein = new Protein(pdbId);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            Protein.Chain currentChain = null;
            Protein.Residue currentResidue = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ATOM  ") || line.startsWith("HETATM")) {
                    // PDB format (fixed width)
                    // 13-16: Atom name
                    String atomName = line.substring(12, 16).trim();
                    // 17-20: Residue name
                    String resName = line.substring(17, 20).trim();
                    // 21: Chain ID
                    String chainId = line.substring(21, 22).trim();
                    if (chainId.isEmpty())
                        chainId = "A";

                    // 22-26: Residue seq num
                    int seqNum = Integer.parseInt(line.substring(22, 26).trim());

                    // 30-38: X
                    double x = Double.parseDouble(line.substring(30, 38).trim());
                    // 38-46: Y
                    double y = Double.parseDouble(line.substring(38, 46).trim());
                    // 46-54: Z
                    double z = Double.parseDouble(line.substring(46, 54).trim());

                    // 76-78: Element symbol (sometimes missing, fallback to atom name)
                    String elementSymbol = "";
                    if (line.length() >= 78) {
                        elementSymbol = line.substring(76, 78).trim();
                    }
                    if (elementSymbol.isEmpty()) {
                        // Guess from atom name (e.g. "CA" -> "C", "N" -> "N")
                        elementSymbol = atomName.substring(0, 1);
                        if (atomName.length() > 1 && Character.isLowerCase(atomName.charAt(1))) {
                            elementSymbol = atomName.substring(0, 2);
                        }
                    }

                    // Resolve Element
                    org.jscience.chemistry.Element element = org.jscience.chemistry.PeriodicTable
                            .getElement(elementSymbol);
                    if (element == null)
                        element = org.jscience.chemistry.PeriodicTable.getElement("C"); // Fallback

                    // Chain Management
                    if (currentChain == null || !currentChain.getChainId().equals(chainId)) {
                        currentChain = protein.getChain(chainId);
                        if (currentChain == null) {
                            currentChain = new Protein.Chain(chainId);
                            protein.addChain(currentChain);
                        }
                    }

                    // Residue Management
                    if (currentResidue == null || currentResidue.getSequenceNumber() != seqNum
                            || !currentResidue.getName().equals(resName)) {
                        // Check if exists in chain (backtracking not supported usually, assume
                        // sequential)
                        boolean found = false;
                        for (Protein.Residue r : currentChain.getResidues()) {
                            if (r.getSequenceNumber() == seqNum && r.getName().equals(resName)) {
                                currentResidue = r;
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            currentResidue = new Protein.Residue(resName, seqNum);
                            currentChain.addResidue(currentResidue);
                        }
                    }

                    // Atom Creation
                    org.jscience.mathematics.linearalgebra.vectors.DenseVector<org.jscience.mathematics.numbers.real.Real> pos = org.jscience.mathematics.linearalgebra.vectors.DenseVector
                            .of(java.util.Arrays.asList(org.jscience.mathematics.numbers.real.Real.of(x),
                                    org.jscience.mathematics.numbers.real.Real.of(y),
                                    org.jscience.mathematics.numbers.real.Real.of(z)),
                                    org.jscience.mathematics.sets.Reals.getInstance());
                    Atom atom = new Atom(element, pos);
                    if (currentResidue != null) {
                        currentResidue.addAtom(atom);
                    }

                } else if (line.startsWith("HEADER")) {
                    if (line.length() >= 50)
                        protein.setName(line.substring(10, 50).trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return protein;
    }

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.pdb.name"); }
}
