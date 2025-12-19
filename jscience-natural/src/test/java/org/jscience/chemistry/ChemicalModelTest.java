package org.jscience.chemistry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
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
        Atom h = new Atom(PeriodicTable.bySymbol("H").get(), zero());
        assertEquals("H", h.getElement().getSymbol());
        // Mass > 0 kg
        assertTrue(h.getMass().to(Units.KILOGRAM).getValue().doubleValue() > 0.0);

        // Test custom position
        // h.setPosition(new Vector3D(1.0, 0.0, 0.0)); // Vector3D is gone? Use
        // Vector<Real>
        Vector<Real> pos = DenseVector.of(List.of(Real.ONE, Real.ZERO, Real.ZERO), Reals.getInstance());
        h.setPosition(pos);
        assertEquals(1.0, h.getPosition().get(0).doubleValue(), 0.001);
    }

    @Test
    public void testMoleculeWeight() {
        // Manual Water creation (factory methods removed)
        Molecule water = new Molecule("Water");
        Atom o = new Atom(PeriodicTable.bySymbol("O").get(), zero());
        Atom h1 = new Atom(PeriodicTable.bySymbol("H").get(), zero());
        Atom h2 = new Atom(PeriodicTable.bySymbol("H").get(), zero());
        water.addAtom(o);
        water.addAtom(h1);
        water.addAtom(h2);

        Quantity<Mass> mw = water.getMolecularWeight();
        // O(15.999) + 2*H(1.008) = 18.015 u. Convert to kg approx 2.99e-26
        assertTrue(mw.to(Units.KILOGRAM).getValue().doubleValue() > 2.9e-26);
    }

    @Test
    public void testReactionStoichiometry() {
        // 2H2 + O2 -> 2H2O
        ChemicalReaction reaction = ChemicalReaction.parse("2H2 + O2 -> 2H2O");
        assertTrue(reaction.isBalanced());

        Quantity<AmountOfSubstance> h2Moles = Quantities.create(2.0, Units.MOLE);
        Quantity<AmountOfSubstance> waterMoles = reaction.stoichiometry("H2", h2Moles, "H2O");

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
