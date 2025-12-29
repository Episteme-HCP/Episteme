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

package org.jscience.medicine.epidemiology;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Time;
import org.jscience.mathematics.numbers.real.Real;
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
        assertEquals(2.5, sir.getR0().doubleValue(), 1e-4);
        assertEquals(0.6, sir.getHerdImmunityThreshold().doubleValue(), 1e-4); // 1 - 1/2.5 = 1 - 0.4 = 0.6
    }

    @Test
    public void testSimulationEquality() {
        // Ensure simulation runs without crashing and produces sensible output
        int pop = 1000;
        int init = 1;
        SIRModel sir = SIRModel.covid19Like(pop, init);

        Quantity<Time> duration = Quantities.create(10, Units.DAY);
        Quantity<Time> dt = Quantities.create(0.1, Units.DAY);

        Real[][] results = sir.simulate(duration, dt);

        assertNotNull(results);
        assertTrue(results.length > 90); // 10 days / 0.1 step ~ 100 steps

        // Check conservation of population roughly
        Real[] finalState = results[results.length - 1];
        double S = finalState[1].doubleValue();
        double I = finalState[2].doubleValue();
        double R = finalState[3].doubleValue();
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
