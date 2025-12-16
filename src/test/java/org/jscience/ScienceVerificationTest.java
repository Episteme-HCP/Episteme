package org.jscience;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.chemistry.PeriodicTable;
import org.jscience.chemistry.Element;
import org.jscience.physics.classical.mechanics.Kinematics;
import org.jscience.biology.genetics.BioSequence;
import org.jscience.biology.genetics.SequenceAlignment;
import org.jscience.earth.coordinates.GeodeticCoordinate;
import org.jscience.earth.coordinates.ReferenceEllipsoid;
import org.jscience.computing.ai.neuralnetwork.Layer;
import org.jscience.computing.ai.neuralnetwork.ActivationFunction;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Energy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.VectorFactory;

import java.util.List;
import java.util.ArrayList;

public class ScienceVerificationTest {

    @Test
    public void testChemistryPeriodicTable() {
        System.out.println("Verifying Chemistry...");
        Element carbon = PeriodicTable.bySymbol("C").get();
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
        Vector<Real> ecef = coord.toECEF();

        double a = ReferenceEllipsoid.WGS84.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();

        // At (0,0,0), x should be 'a', y=0, z=0
        assertEquals(a, ecef.get(0).doubleValue(), 1.0);
        assertEquals(0.0, ecef.get(1).doubleValue(), 1.0);
        assertEquals(0.0, ecef.get(2).doubleValue(), 1.0);
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
