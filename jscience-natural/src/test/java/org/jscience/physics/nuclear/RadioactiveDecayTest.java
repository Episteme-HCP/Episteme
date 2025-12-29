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

package org.jscience.physics.nuclear;

import org.jscience.mathematics.numbers.real.Real;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RadioactiveDecayTest {

    @Test
    public void testDecayConstant() {
        // T1/2 = 100s
        // lambda = ln(2)/100 = 0.693147 / 100 = 0.006931
        RadioactiveDecay decay = new RadioactiveDecay(
                Real.of(100),
                RadioactiveDecay.DecayType.ALPHA,
                Real.ZERO);
        assertEquals(0.00693147, decay.getDecayConstant().doubleValue(), 1e-6);
    }

    @Test
    public void testRemainingNuclei() {
        // After 1 half-life, should be 50%
        RadioactiveDecay decay = RadioactiveDecay.CARBON_14;
        Real remaining = decay.remainingNuclei(Real.of(1000), decay.getHalfLife());
        assertEquals(500.0, remaining.doubleValue(), 1e-1);
    }

    @Test
    public void testCarbonDating() {
        // If ratio is 0.5, age should be 5730 years
        Real age = RadioactiveDecay.carbonDate(Real.of(0.5));
        assertEquals(5730.0, age.doubleValue(), 1.0);

        // If ratio is 0.25, age should be 11460 years
        age = RadioactiveDecay.carbonDate(Real.of(0.25));
        assertEquals(11460.0, age.doubleValue(), 1.0);
    }
}