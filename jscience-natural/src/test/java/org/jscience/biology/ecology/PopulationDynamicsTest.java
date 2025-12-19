package org.jscience.biology.ecology;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Dimensionless;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;

/**
 * Tests for PopulationDynamics.
 */
public class PopulationDynamicsTest {

    @Test
    public void testExponentialGrowth() {
        double p0 = 100;
        double r = 0.1;
        double t = 10;
        double expected = 100 * Math.exp(1); // e^1 ~ 2.718 -> 271.8

        double result = PopulationDynamics.exponentialGrowth(p0, r, t);
        assertEquals(expected, result, 1e-5);
    }

    @Test
    public void testExponentialGrowthQuantity() {
        Quantity<Dimensionless> p0 = Quantities.create(100, Units.ONE);
        Quantity<Frequency> r = Quantities.create(0.1, Units.HERTZ); // 0.1 / s
        Quantity<Time> t = Quantities.create(10, Units.SECOND);

        Quantity<Dimensionless> res = PopulationDynamics.exponentialGrowth(p0, r, t);
        assertEquals(100 * Math.exp(1), res.getValue().doubleValue(), 1e-5);
    }

    @Test
    public void testLogisticGrowth() {
        double p0 = 10;
        double K = 100;
        double r = 0.5;
        double t = 2;

        double res = PopulationDynamics.logisticGrowth(p0, r, K, t);
        // P(t) = 100 / (1 + (9)*e^-1) = 100 / (1 + 9*0.3678) = 100 / (1+3.31) =
        // 100/4.31 = 23.2
        double expected = 100.0 / (1.0 + ((100.0 - 10.0) / 10.0) * Math.exp(-0.5 * 2.0));
        assertEquals(expected, res, 1e-5);
    }

    @Test
    public void testLogisticGrowthQuantity() {
        Quantity<Dimensionless> p0 = Quantities.create(10, Units.ONE);
        Quantity<Dimensionless> K = Quantities.create(100, Units.ONE);
        Quantity<Frequency> r = Quantities.create(0.5, Units.HERTZ);
        Quantity<Time> t = Quantities.create(2, Units.SECOND);

        Quantity<Dimensionless> res = PopulationDynamics.logisticGrowth(p0, r, K, t);
        double expected = 100.0 / (1.0 + 9.0 * Math.exp(-1.0));
        assertEquals(expected, res.getValue().doubleValue(), 1e-5);
    }
}
