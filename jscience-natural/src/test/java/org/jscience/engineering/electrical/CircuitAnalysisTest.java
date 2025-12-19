package org.jscience.engineering.electrical;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricPotential;
import org.jscience.measure.quantity.ElectricCurrent;
import org.jscience.measure.quantity.ElectricResistance;
import org.jscience.measure.quantity.Power;

/**
 * Tests for CircuitAnalysis.
 */
public class CircuitAnalysisTest {

    @Test
    public void testOhmsLawQuantity() {
        Quantity<ElectricCurrent> i = Quantities.create(2, Units.AMPERE);
        Quantity<ElectricResistance> r = Quantities.create(10, Units.OHM);

        Quantity<ElectricPotential> v = CircuitAnalysis.voltage(i, r);
        assertEquals(20.0, v.to(Units.VOLT).getValue().doubleValue(), 1e-9);
    }

    @Test
    public void testPowerQuantity() {
        Quantity<ElectricPotential> v = Quantities.create(12, Units.VOLT);
        Quantity<ElectricCurrent> i = Quantities.create(0.5, Units.AMPERE);

        Quantity<Power> p = CircuitAnalysis.power(v, i);
        assertEquals(6.0, p.to(Units.WATT).getValue().doubleValue(), 1e-9);
    }

    @Test
    public void testParallelResistance() {
        // 10 || 10 = 5
        double rTotal = CircuitAnalysis.resistanceParallel(10, 10);
        assertEquals(5.0, rTotal, 1e-9);
    }
}
