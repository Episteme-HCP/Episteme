/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.biology;

import org.jscience.natural.chemistry.Molecule;
import org.jscience.natural.chemistry.ChemicalReaction;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a biochemical reaction converting reactants to products.
 * <p>
 * This class extends the core {@link ChemicalReaction} with support for
 * catalysts (enzymes) and detailed {@link Molecule} linking.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class BioChemicalReaction extends ChemicalReaction {
    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<BioReactant> bioReactants = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<BioProduct> bioProducts = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Molecule> catalysts = new ArrayList<>();

    @Attribute
    private boolean reversible;

    public BioChemicalReaction(String name) {
        super(new HashMap<>(), new HashMap<>(), name);
    }

    public void addReactant(Molecule molecule, double stoichiometry) {
        bioReactants.add(new BioReactant(molecule, stoichiometry));
        // Keep parent ChemicalReaction in sync (using molecule name as key)
        getReactants().put(molecule.getName(), (int) Math.round(stoichiometry));
    }

    public void addProduct(Molecule molecule, double stoichiometry) {
        bioProducts.add(new BioProduct(molecule, stoichiometry));
        // Keep parent ChemicalReaction in sync
        getProducts().put(molecule.getName(), (int) Math.round(stoichiometry));
    }

    public void addCatalyst(Molecule enzyme) {
        catalysts.add(enzyme);
    }

    public List<BioReactant> getBioReactants() {
        return bioReactants;
    }

    public List<BioProduct> getBioProducts() {
        return bioProducts;
    }

    public List<Molecule> getCatalysts() {
        return catalysts;
    }

    public boolean isReversible() {
        return reversible;
    }

    public void setReversible(boolean reversible) {
        this.reversible = reversible;
    }

    @Persistent
    public static class BioReactant {
        @Relation(type = Relation.Type.MANY_TO_ONE)
        private final Molecule molecule;
        @Attribute
        private final double stoichiometry;

        public BioReactant(Molecule molecule, double stoichiometry) {
            this.molecule = molecule;
            this.stoichiometry = stoichiometry;
        }

        public Molecule getMolecule() { return molecule; }
        public double getStoichiometry() { return stoichiometry; }
    }

    @Persistent
    public static class BioProduct {
        @Relation(type = Relation.Type.MANY_TO_ONE)
        private final Molecule molecule;
        @Attribute
        private final double stoichiometry;

        public BioProduct(Molecule molecule, double stoichiometry) {
            this.molecule = molecule;
            this.stoichiometry = stoichiometry;
        }

        public Molecule getMolecule() { return molecule; }
        public double getStoichiometry() { return stoichiometry; }
    }

    @Override
    public String toString() {
        return String.format("BioChemicalReaction[%s, %d bio-reactants \u2192 %d bio-products]",
            getName(), bioReactants.size(), bioProducts.size());
    }
}


