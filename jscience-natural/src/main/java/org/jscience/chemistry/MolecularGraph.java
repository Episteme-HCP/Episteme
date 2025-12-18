package org.jscience.chemistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a molecule as a graph of atoms and bonds.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class MolecularGraph {

    private final List<Atom> atoms;
    private final List<Bond> bonds;

    public MolecularGraph() {
        this.atoms = new ArrayList<>();
        this.bonds = new ArrayList<>();
    }

    public void addAtom(Atom atom) {
        atoms.add(atom);
    }

    public void addBond(Atom a, Atom b, BondType type) {
        bonds.add(new Bond(a, b, type));
    }

    public List<Atom> getAtoms() {
        return new ArrayList<>(atoms);
    }

    public List<Bond> getBonds() {
        return new ArrayList<>(bonds);
    }

    /**
     * connection table: Map<Atom, List<Bond>>
     */
    public Map<Atom, List<Bond>> getConnections() {
        Map<Atom, List<Bond>> connections = new HashMap<>();
        for (Atom a : atoms) {
            connections.put(a, new ArrayList<>());
        }
        for (Bond b : bonds) {
            connections.get(b.source).add(b);
            connections.get(b.target).add(b);
        }
        return connections;
    }

    /**
     * Calculates molecular mass.
     */
    public double getMolecularMass() {
        return atoms.stream().mapToDouble(a -> a.getElement().getAtomicMass().getValue().doubleValue()).sum();
    }

    /**
     * Returns the formula (e.g. C6H6).
     */
    public String getFormula() {
        Map<String, Integer> counts = new HashMap<>();
        for (Atom a : atoms) {
            counts.put(a.getElement().getSymbol(), counts.getOrDefault(a.getElement().getSymbol(), 0) + 1);
        }
        // Improve ordering: C first, H second, then alphabetical
        List<String> symbols = new ArrayList<>(counts.keySet());
        symbols.sort((s1, s2) -> {
            if (s1.equals("C"))
                return -1;
            if (s2.equals("C"))
                return 1;
            if (s1.equals("H"))
                return -1;
            if (s2.equals("H"))
                return 1;
            return s1.compareTo(s2);
        });

        StringBuilder sb = new StringBuilder();
        for (String s : symbols) {
            sb.append(s);
            int count = counts.get(s);
            if (count > 1)
                sb.append(count);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormula() + " (" + atoms.size() + " atoms, " + bonds.size() + " bonds)";
    }

    public static class Bond {
        public final Atom source;
        public final Atom target;
        public final BondType type;

        public Bond(Atom source, Atom target, BondType type) {
            this.source = source;
            this.target = target;
            this.type = type;
        }

        @Override
        public String toString() {
            return source.getElement().getSymbol() + "-" + type + "-" + target.getElement().getSymbol();
        }
    }

    public enum BondType {
        SINGLE, DOUBLE, TRIPLE, AROMATIC
    }
}
