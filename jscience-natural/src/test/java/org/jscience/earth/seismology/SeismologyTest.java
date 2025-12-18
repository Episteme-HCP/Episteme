/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.earth.seismology;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.quantity.Length;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeismologyTest {

    @Test
    public void testMagnitudeToMoment() {
        // Magnitude 5.0
        // log10(M0) = 1.5*5 + 16.1 = 7.5 + 16.1 = 23.6 (dyne-cm)
        // M0_cgs = 10^23.6 = 3.98e23
        // M0_si = 3.98e16 Nm
        Real moment = Seismology.magnitudeToMoment(Real.of(5.0));
        assertEquals(3.981e16, moment.doubleValue(), 1e14);
    }

    @Test
    public void testEnergyReleased() {
        // Magnitude 5.0
        // logE = 1.5*5 + 4.8 = 12.3
        // E = 10^12.3 = 1.995e12 Joules
        Quantity<Energy> energy = Seismology.energyReleased(Real.of(5.0));
        assertEquals(1.995e12, energy.getValue().doubleValue(), 1e10);
    }

    @Test
    public void testEnergyRatio() {
        // Magnitude 6 vs 5
        // Ratio = 10^(1.5 * 1) = 31.62
        Real ratio = Seismology.energyRatio(Real.of(6.0), Real.of(5.0));
        assertEquals(31.6227, ratio.doubleValue(), 1e-4);
    }

    @Test
    public void testIntensity() {
        // Mag 7.0 at 10km distance
        // MMI = 1.5*7 - 3.5*log10(10) + 3 = 10.5 - 3.5 + 3 = 10
        int mmi = Seismology.estimateIntensity(Real.of(7.0), Real.of(10.0));
        assertEquals(10, mmi);
    }
}
