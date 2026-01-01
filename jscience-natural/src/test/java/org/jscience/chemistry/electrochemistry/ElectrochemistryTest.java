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

package org.jscience.chemistry.electrochemistry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.ElectricPotential;
import org.jscience.measure.quantity.Temperature;

public class ElectrochemistryTest {

    @Test
    public void testDaniellCell() {
        HalfCell zinc = new HalfCell("Zn2+ + 2e- -> Zn", -0.76);
        HalfCell copper = new HalfCell("Cu2+ + 2e- -> Cu", 0.34);

        // Zinc is anode (lower potential), Copper is cathode
        GalvanicCell cell = new GalvanicCell(zinc, copper);

        Quantity<ElectricPotential> E0 = cell.getStandardPotential();
        System.out.println("Daniell Cell EÃ‚Â°: " + E0);

        assertEquals(1.10, E0.to(Units.VOLT).getValue().doubleValue(), 1e-9);
    }

    @Test
    public void testNernstEquation() {
        // E0 = 1.10 V
        Quantity<ElectricPotential> E0 = Quantities.create(1.10, Units.VOLT);

        // [Zn2+] = 0.1 M, [Cu2+] = 1.0 M
        // Q = 0.1 / 1.0 = 0.1
        double Q = 0.1;
        int n = 2; // 2 electrons

        Quantity<ElectricPotential> E = NernstEquation.calculatePotentialAt25C(E0, n, Q);
        System.out.println("Nernst Potential (Q=0.1): " + E);

        // Expected: 1.10 - (0.05916/2)*log10(0.1) = 1.10 + 0.02958 = 1.12958
        double expected = 1.10 + (0.05916 / 2.0);
        assertEquals(expected, E.to(Units.VOLT).getValue().doubleValue(), 1e-5);
    }

    @Test
    public void testNernstEquationTemperature() {
        // Same cell at 373 K (100 C)
        Quantity<ElectricPotential> E0 = Quantities.create(1.10, Units.VOLT);
        Quantity<Temperature> T = Quantities.create(373.15, Units.KELVIN);
        double Q = 0.1;
        int n = 2;

        Quantity<ElectricPotential> E = NernstEquation.calculatePotential(E0, T, n, Q);
        System.out.println("Nernst Potential (100Ã‚Â°C): " + E);

        // E = 1.10 - (R*T / nF) * ln(Q)
        // ln(0.1) = -2.302585
        // Factor = 8.314... * 373.15 / (2 * 96485) = 3102.5 / 192970 = 0.01607
        // E = 1.10 - 0.01607 * (-2.3) = 1.10 + 0.037

        assertTrue(E.to(Units.VOLT).getValue().doubleValue() > 1.10);
    }
}


