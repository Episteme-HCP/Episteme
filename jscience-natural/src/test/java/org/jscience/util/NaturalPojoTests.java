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

package org.jscience.util;

import org.junit.jupiter.api.Test;
import org.jscience.biology.*;
import org.jscience.biology.biochemistry.*;
import org.jscience.biology.cell.*;
import org.jscience.chemistry.*;
import org.jscience.chemistry.crystallography.*;
import org.jscience.physics.astronomy.*;
import org.jscience.physics.classical.mechanics.*;
import org.jscience.physics.classical.thermodynamics.*;

public class NaturalPojoTests {

    @Test
    public void testBiologyPojos() {
        PojoTester.testPojo(DNA.class);
        PojoTester.testPojo(RNA.class);
        PojoTester.testPojo(Base.class);
        PojoTester.testPojo(Human.class);
        PojoTester.testPojo(Individual.class);
        PojoTester.testPojo(Virus.class);
        PojoTester.testPojo(VirusSpecies.class);

        // Biochemistry & Cell
        PojoTester.testPojo(EnzymeKinetics.class);
        PojoTester.testPojo(CellCycle.class);
        PojoTester.testPojo(Organelle.class);
        PojoTester.testPojo(Metabolism.class);
    }

    @Test
    public void testChemistryPojos() {
        PojoTester.testPojo(Atom.class);
        PojoTester.testPojo(Bond.class);
        PojoTester.testPojo(Molecule.class);
        PojoTester.testPojo(Element.class);
        PojoTester.testPojo(Isotope.class);

        // Crystallography
        PojoTester.testPojo(UnitCell.class);
        PojoTester.testPojo(MillerIndices.class);
    }

    @Test
    public void testPhysicsPojos() {
        // Mechanics
        PojoTester.testPojo(Particle.class);
        PojoTester.testPojo(RigidBody.class);
        PojoTester.testPojo(ProjectileMotion.class);

        // Astronomy
        PojoTester.testPojo(Star.class);
        PojoTester.testPojo(Planet.class);
        // PojoTester.testPojo(CelestialBody.class); // Abstract?
        PojoTester.testPojo(OrbitalElements.class);

        // Thermodynamics
        PojoTester.testPojo(IdealGas.class);
        PojoTester.testPojo(ThermodynamicProperties.class);
    }
}


