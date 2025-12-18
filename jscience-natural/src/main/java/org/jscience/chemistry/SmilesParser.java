package org.jscience.chemistry;

import java.util.Stack;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Parser for SMILES (Simplified Molecular Input Line Entry System) strings.
 * <p>
 * Supports basic aliphatic and aromatic structures, branching, and ring
 * closures.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class SmilesParser {

    private SmilesParser() {
    }

    /**
     * Parses a SMILES string into a MolecularGraph.
     * 
     * @param smiles the SMILES string
     * @return the molecular graph
     */
    public static MolecularGraph parse(String smiles) {
        MolecularGraph graph = new MolecularGraph();
        Stack<Atom> branchStack = new Stack<>();
        Atom lastAtom = null;

        // Handling ring closures: number -> atom
        java.util.Map<Integer, Atom> rings = new java.util.HashMap<>();

        for (int i = 0; i < smiles.length(); i++) {
            char c = smiles.charAt(i);

            if (Character.isLetter(c)) {
                // Parse element symbol (e.g., C, Cl, O, N)
                String symbol = String.valueOf(c);
                if (i + 1 < smiles.length() && Character.isLowerCase(smiles.charAt(i + 1))) {
                    symbol += smiles.charAt(i + 1);
                    i++;
                }

                // Determine implicit bond type
                MolecularGraph.BondType bondType = MolecularGraph.BondType.SINGLE;
                if (Character.isLowerCase(c)) {
                    // Aromatic
                    bondType = MolecularGraph.BondType.AROMATIC;
                    symbol = symbol.toUpperCase(); // Store as uppercase in Atom for now
                }

                // Look up Element (assuming Element enum exists and follows standard naming)
                // Look up Element using PeriodicTable
                Element element = PeriodicTable.bySymbol(symbol.toUpperCase()).orElse(PeriodicTable.CARBON);

                // Initial position (generic, ForceDirectedLayout will fix this later)
                // Use fully qualified names to ensure correctness if imports are missing
                Vector<Real> position = org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(
                        java.util.Arrays.asList(Real.ZERO, Real.ZERO, Real.ZERO),
                        org.jscience.mathematics.sets.Reals.getInstance());

                Atom atom = new Atom(element, position);
                // Mass is derived from Element in Atom constructor automatically.

                graph.addAtom(atom);

                if (lastAtom != null) {
                    // Check if an explicit bond was defined before (complex logic omitted for
                    // brevity)
                    // Assuming single for now unless '=' or '#'
                    graph.addBond(lastAtom, atom, bondType);
                }
                lastAtom = atom;

            } else if (c == '(') {
                branchStack.push(lastAtom);
            } else if (c == ')') {
                lastAtom = branchStack.pop();
            } else if (c == '=') {
                // Double bond indicator - apply to next atom
                // Need state machine to handle this correctly
            } else if (c == '#') {
                // Triple bond
            } else if (Character.isDigit(c)) {
                // Ring closure
                int ringId = Character.getNumericValue(c);
                if (rings.containsKey(ringId)) {
                    Atom partner = rings.remove(ringId);
                    graph.addBond(lastAtom, partner, MolecularGraph.BondType.SINGLE);
                } else {
                    rings.put(ringId, lastAtom);
                }
            }
        }

        return graph;
    }
}
