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

package org.jscience.medicine.pharmacology;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Frequency;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.MassDensity;
import org.jscience.measure.quantity.Time;
import org.jscience.measure.quantity.Volume;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PharmacokineticsTest {

    @Test
    public void testHalfLife() {
        // T1/2 = 2 hours
        Quantity<Time> halfLife = Quantities.create(2, Units.HOUR);
        Quantity<Volume> vd = Quantities.create(10, Units.LITRE);

        Pharmacokinetics pk = Pharmacokinetics.fromHalfLife(halfLife, vd, 1.0);

        // Check if calculated half life matches input
        assertEquals(2.0, pk.getHalfLife().to(Units.HOUR).getValue().doubleValue(), 1e-4);

        // ke should be ln(2)/2 = 0.3465 / h
        // 0.3465/h = 0.3465/3600 / s = 9.626e-5 Hz
        assertEquals(0.34657, pk.getKe().to(Units.HERTZ).getValue().doubleValue() * 3600, 1e-4);
    }

    @Test
    public void testConcentrationAfterBolus() {
        // ke = 0.1/h, Vd = 10L, Dose = 100mg
        // C(0) = 10 mg/L
        Quantity<Frequency> ke = Quantities.create(0.1 / 3600.0, Units.HERTZ); // 0.1 per hour
        Quantity<Volume> vd = Quantities.create(10, Units.LITRE);
        Quantity<Mass> dose = Quantities.create(100, Units.GRAM).divide(1000).asType(Mass.class); // 100 mg

        Pharmacokinetics pk = new Pharmacokinetics(ke, vd, 1.0);

        Quantity<MassDensity> c0 = pk.concentrationAfterBolus(dose, Quantities.create(0, Units.SECOND))
                .asType(MassDensity.class);
        // Expect 100mg / 10L = 10 mg/L = 0.01 g/L
        // 0.01 kg/m^3 (SI) ? No, 1 g/L = 1 kg/m^3.
        // 10 mg/L = 0.01 g/L = 0.01 kg/m^3.

        // Let's check value in standard units.
        // JScience Quantity often defaults to SI. Mass=kg, Vol=m^3.
        // 100 mg = 1e-4 kg
        // 10 L = 1e-2 m^3
        // Result = 1e-2 kg/m^3.

        // Let's strictly convert to a known unit if possible, or check SI value.
        // 10 mg/L = 10 ppm approx.

        // We can just verify the numeric value roughly if we trust base units.
        assertEquals(0.01, c0.to(Units.KILOGRAM_PER_CUBIC_METER).getValue().doubleValue(), 1e-6);
    }
}


