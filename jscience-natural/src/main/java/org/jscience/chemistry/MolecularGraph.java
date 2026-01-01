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

package org.jscience.chemistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jscience.mathematics.discrete.Graph;

/**
 * Represents a molecule as a graph of atoms and bonds.
 * <p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularGraph implements Graph<Atom> {

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

    // --- Graph<Atom> Interface Implementation ---

    @Override
    public Set<Atom> vertices() {
        return new HashSet<>(atoms);
    }

    @Override
    public Set<Graph.Edge<Atom>> edges() {
        Set<Graph.Edge<Atom>> edgeSet = new HashSet<>();
        for (Bond b : bonds) {
            edgeSet.add(new Graph.Edge<Atom>() {
                @Override
                public Atom source() {
                    return b.source;
                }

                @Override
                public Atom target() {
                    return b.target;
                }
            });
        }
        return edgeSet;
    }

    @Override
    public boolean addVertex(Atom vertex) {
        if (atoms.contains(vertex))
            return false;
        atoms.add(vertex);
        return true;
    }

    @Override
    public boolean addEdge(Atom source, Atom target) {
        // Default to SINGLE bond when using Graph interface
        addBond(source, target, BondType.SINGLE);
        return true;
    }

    @Override
    public Set<Atom> neighbors(Atom vertex) {
        Set<Atom> neighborSet = new HashSet<>();
        for (Bond b : bonds) {
            if (b.source.equals(vertex))
                neighborSet.add(b.target);
            if (b.target.equals(vertex))
                neighborSet.add(b.source);
        }
        return neighborSet;
    }

    @Override
    public int degree(Atom vertex) {
        return neighbors(vertex).size();
    }

    @Override
    public boolean isDirected() {
        return false; // Molecular bonds are undirected
    }

    @Override
    public int vertexCount() {
        return atoms.size();
    }

    // --- Inner Classes ---

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


