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

package org.jscience.natural;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.natural.chemistry.PeriodicTable;
import org.jscience.natural.chemistry.Element;
import org.jscience.natural.physics.classical.mechanics.Kinematics;
import org.jscience.natural.biology.genetics.BioSequence;
import org.jscience.natural.biology.genetics.SequenceAlignment;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.natural.earth.coordinates.ReferenceEllipsoid;
import org.jscience.natural.computing.ai.neuralnetwork.Layer;
import org.jscience.natural.computing.ai.neuralnetwork.ActivationFunction;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Mass;
import org.jscience.core.measure.quantity.Energy;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.vectors.VectorFactory;

import java.util.List;
import java.util.ArrayList;

public class ScienceVerificationTest {

    @Test
    public void testChemistryPeriodicTable() {
        System.out.println("Verifying Chemistry...");
        Element carbon = PeriodicTable.bySymbol("C");
        assertNotNull(carbon, "Carbon should be found");
        assertEquals(6, carbon.getAtomicNumber());
        System.out.println("Carbon mass: " + carbon.getAtomicMass());
    }

    @Test
    public void testPhysicsKinematics() {
        System.out.println("Verifying Physics...");

        List<Real> vData = new ArrayList<>();
        vData.add(Real.of(3.0));
        vData.add(Real.of(4.0));
        vData.add(Real.of(0.0));
        Vector<Real> velocity = VectorFactory.create(vData, Real.ZERO);

        Quantity<Mass> massQ = Quantities.create(10.0, Units.KILOGRAM);
        Quantity<Energy> ke = Kinematics.kineticEnergy(massQ, velocity);
        // KE = 0.5 * 10 * (3^2 + 4^2) = 5 * 25 = 125
        assertEquals(125.0, ke.to(Units.JOULE).getValue().doubleValue(), 0.001);
        System.out.println("Kinetic Energy: " + ke);
    }

    @Test
    public void testBiologyGenetics() {
        System.out.println("Verifying Biology...");
        BioSequence dna = new BioSequence("ATGC", BioSequence.Type.DNA);
        BioSequence rna = dna.transcribe();
        assertEquals("AUGC", rna.getSequence());

        SequenceAlignment.AlignmentResult result = SequenceAlignment.alignGlobal("ATGC", "ATGG", 1, -1, -1);
        System.out.println("Alignment Score: " + result.score);
        assertTrue(result.score > 0);
    }

    @Test
    public void testEarthCoordinates() {
        System.out.println("Verifying Earth Coordinates...");
        GeodeticCoordinate coord = new GeodeticCoordinate(0, 0, 0); // Equator, Prime Meridian, Sea Level
        org.jscience.natural.earth.coordinates.ECEFCoordinate ecef = coord.toECEF();

        // Legacy bridge: reference ellipsoid uses legacy units.
        double a = ReferenceEllipsoid.WGS84.getSemiMajorAxis()
                .to(org.jscience.core.measure.Units.METER).getValue().doubleValue();

        // At (0,0,0), x should be 'a', y=0, z=0
        assertEquals(a, ecef.getX().to(org.jscience.core.measure.Units.METER).getValue().doubleValue(), 1.0);
        assertEquals(0.0, ecef.getY().to(org.jscience.core.measure.Units.METER).getValue().doubleValue(), 1.0);
        assertEquals(0.0, ecef.getZ().to(org.jscience.core.measure.Units.METER).getValue().doubleValue(), 1.0);
        System.out.println("ECEF: " + ecef);
    }

    @Test
    public void testComputingNeuralNetwork() {
        System.out.println("Verifying Computing AI...");
        Layer layer = new Layer(3, 2, ActivationFunction.RELU);

        List<Real> inputList = new ArrayList<>();
        inputList.add(Real.of(1.0));
        inputList.add(Real.of(1.0));
        inputList.add(Real.of(1.0));
        Vector<Real> input = VectorFactory.create(inputList, Real.ZERO);

        Vector<Real> output = layer.forward(input);
        assertEquals(2, output.dimension());
        System.out.println("Layer Output: " + output);
    }
}





