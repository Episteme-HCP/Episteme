package org.jscience.biology.io;

import org.jscience.biology.structure.Protein;

import org.jscience.chemistry.Atom;
import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Loads protein structures from PDB format (Simplified).
 */
public class GenericPDBLoader {

    /**
     * Parses a PDB input stream into a protein structure.
     */
    public static Protein loadPDB(String pdbId, InputStream is) {
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
                    Element element = PeriodicTable.getElement(elementSymbol);
                    if (element == null)
                        element = PeriodicTable.getElement("C"); // Fallback

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
                    DenseVector<Real> pos = DenseVector.of(java.util.Arrays.asList(Real.of(x), Real.of(y), Real.of(z)),
                            Reals.getInstance());
                    Atom atom = new Atom(element, pos);
                    currentResidue.addAtom(atom);

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
}
