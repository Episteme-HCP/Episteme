package org.jscience.medicine.epidemiology;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SIRModelTest {

    @Test
    public void testBasicParams() {
        int pop = 1000;
        int init = 1;
        // Beta = 0.5/day, Gamma = 0.2/day
        Quantity<Frequency> beta = Quantities.create(0.5 / 86400, Units.HERTZ);
        Quantity<Frequency> gamma = Quantities.create(0.2 / 86400, Units.HERTZ);

        SIRModel sir = new SIRModel(pop, init, beta, gamma);

        assertEquals(pop, sir.getPopulation());
        assertEquals(2.5, sir.getR0(), 1e-4);
        assertEquals(0.6, sir.getHerdImmunityThreshold(), 1e-4); // 1 - 1/2.5 = 1 - 0.4 = 0.6
    }

    @Test
    public void testSimulationEquality() {
        // Ensure simulation runs without crashing and produces sensible output
        int pop = 1000;
        int init = 1;
        SIRModel sir = SIRModel.covid19Like(pop, init);

        Quantity<Time> duration = Quantities.create(10, Units.DAY);
        Quantity<Time> dt = Quantities.create(0.1, Units.DAY);

        double[][] results = sir.simulate(duration, dt);

        assertNotNull(results);
        assertTrue(results.length > 90); // 10 days / 0.1 step ~ 100 steps

        // Check conservation of population roughly
        double[] finalState = results[results.length - 1];
        double S = finalState[1];
        double I = finalState[2];
        double R = finalState[3];
        assertEquals(pop, S + I + R, 1e-1); // Euler method drift might exist but small
    }

    @Test
    public void testPeakTime() {
        SIRModel sir = SIRModel.measlesLike(1000, 1);
        Quantity<Time> tPeak = sir.getPeakTime();
        assertNotNull(tPeak);
        assertTrue(tPeak.getValue().doubleValue() > 0);
    }
}
