package org.jscience.chemistry;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.mathematics.numbers.real.Real;
import java.util.List;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A molecule: collection of atoms connected by bonds.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Molecule {

    private final String name;
    private final List<Atom> atoms;
    private final List<Bond> bonds;
    private final Map<Atom, List<Bond>> atomBonds;

    public Molecule(String name) {
        this.name = name;
        this.atoms = new ArrayList<>();
        this.bonds = new ArrayList<>();
        this.atomBonds = new HashMap<>();
    }

    // --- Building methods ---

    public void addAtom(Atom atom) {
        atoms.add(atom);
        atomBonds.put(atom, new ArrayList<>());
    }

    public void addBond(Bond bond) {
        bonds.add(bond);
        atomBonds.get(bond.getAtom1()).add(bond);
        atomBonds.get(bond.getAtom2()).add(bond);
    }

    // --- Properties ---

    public String getName() {
        return name;
    }

    public List<Atom> getAtoms() {
        return Collections.unmodifiableList(atoms);
    }

    public List<Bond> getBonds() {
        return Collections.unmodifiableList(bonds);
    }

    public int getAtomCount() {
        return atoms.size();
    }

    public int getBondCount() {
        return bonds.size();
    }

    /**
     * Gets all bonds connected to an atom.
     */
    public List<Bond> getBondsFor(Atom atom) {
        return Collections.unmodifiableList(atomBonds.getOrDefault(atom, List.of()));
    }

    /**
     * Gets all neighbors of an atom.
     */
    public List<Atom> getNeighbors(Atom atom) {
        return getBondsFor(atom).stream()
                .map(b -> b.getOtherAtom(atom))
                .collect(Collectors.toList());
    }

    /**
     * Molecular formula (simplified).
     */
    public String getFormula() {
        Map<String, Integer> counts = new HashMap<>();
        for (Atom a : atoms) {
            counts.merge(a.getElement().getSymbol(), 1, (x, y) -> x + y);
        }

        StringBuilder sb = new StringBuilder();
        // Carbon first (Hill system)
        if (counts.containsKey("C")) {
            sb.append("C");
            int c = counts.remove("C");
            if (c > 1)
                sb.append(c);
        }
        if (counts.containsKey("H")) {
            sb.append("H");
            int h = counts.remove("H");
            if (h > 1)
                sb.append(h);
        }
        // Rest alphabetically
        counts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    sb.append(e.getKey());
                    if (e.getValue() > 1)
                        sb.append(e.getValue());
                });

        return sb.toString();
    }

    /**
     * Molecular weight (sum of atomic masses).
     */
    public Quantity<Mass> getMolecularWeight() {
        double totalMassKg = atoms.stream()
                .mapToDouble(a -> a.getMass().to(Units.KILOGRAM).getValue().doubleValue())
                .sum();
        return Quantities.create(totalMassKg, Units.KILOGRAM);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, MW=%.2f u)", name, getFormula(), getMolecularWeight().getValue().doubleValue());
    }
}
