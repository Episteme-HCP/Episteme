package org.jscience.chemistry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

import org.jscience.measure.quantity.AmountOfSubstance;

import org.jscience.chemistry.loaders.ChemistryDataLoader;

import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.sets.Reals;
import java.util.List;

public class ChemicalModelTest {

    private Vector<Real> zero() {
        return DenseVector.of(List.of(Real.ZERO, Real.ZERO, Real.ZERO), Reals.getInstance());
    }

    @Test
    public void testAtomProperties() {
        // Test Hydrogen from PeriodicTable (static load)
        Element h = PeriodicTable.bySymbol("H");
        assertNotNull(h, "Hydrogen should be found");
        assertEquals("H", h.getSymbol());
        // Mass > 0 kg
        assertTrue(h.getAtomicMass().to(Units.KILOGRAM).getValue().doubleValue() > 0.0);

        // Test custom position
        // h.setPosition(new Vector3D(1.0, 0.0, 0.0)); // Vector3D is gone? Use
        // Vector<Real>
        Vector<Real> pos = DenseVector.of(List.of(Real.ONE, Real.ZERO, Real.ZERO), Reals.getInstance());
        Atom atom = new Atom(h, zero());
        atom.setPosition(pos);
        assertEquals(1.0, atom.getPosition().get(0).doubleValue(), 0.001);
    }

    @Test
    public void testMoleculeWeight() {
        // Manual Water creation (factory methods removed)
        Molecule water = new Molecule("Water");
        Element o = PeriodicTable.bySymbol("O");
        Element h = PeriodicTable.bySymbol("H");

        assertNotNull(o, "Oxygen should be found");
        assertNotNull(h, "Hydrogen should be found");

        Atom atomO = new Atom(o, zero());
        Atom atomH1 = new Atom(h, zero());
        Atom atomH2 = new Atom(h, zero());

        water.addAtom(atomO);
        water.addAtom(atomH1);
        water.addAtom(atomH2);

        var mw = water.getMolecularWeight();
        // O(15.999) + 2*H(1.008) = 18.015 u. Convert to kg approx 2.99e-26
        assertTrue(mw.to(Units.KILOGRAM).getValue().doubleValue() > 2.9e-26);
    }

    @Test
    public void testReactionStoichiometry() {
        // 2H2 + O2 -> 2H2O
        ChemicalReaction reaction = ChemicalReaction.parse("2H2 + O2 -> 2H2O");
        assertTrue(reaction.isBalanced());

        Quantity<AmountOfSubstance> h2Moles = Quantities.create(2.0, Units.MOLE);
        var waterMoles = reaction.stoichiometry("H2", h2Moles, "H2O");

        // 2 mol H2 produces 2 mol H2O
        assertEquals(2.0, waterMoles.to(Units.MOLE).getValue().doubleValue(), 0.001);
    }

    @Test
    public void testDataLoading() {
        // Load elements from JSON (should register/overwrite)
        ChemistryDataLoader.loadElements();

        // Check if data is accessible via PeriodicTable
        // The sample JSON had Hydrogen, Helium, Carbon, Oxygen.
        // It shouldn't crash.
        assertNotNull(PeriodicTable.bySymbol("He"));
    }
}
