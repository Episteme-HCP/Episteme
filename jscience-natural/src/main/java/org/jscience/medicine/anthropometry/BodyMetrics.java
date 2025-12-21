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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.medicine.anthropometry;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;

/**
 * Body Mass Index and Body Surface Area calculators.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BodyMetrics {

    private BodyMetrics() {
    }

    /** BMI = mass (kg) / height² (m²) */
    public static Real bmi(Quantity<Mass> mass, Quantity<Length> height) {
        Real m = mass.to(Units.KILOGRAM).getValue();
        Real h = height.to(Units.METER).getValue();
        return m.divide(h.pow(2));
    }

    /** BMI category */
    public static String bmiCategory(Real bmi) {
        double val = bmi.doubleValue();
        if (val < 18.5)
            return "Underweight";
        if (val < 25)
            return "Normal";
        if (val < 30)
            return "Overweight";
        return "Obese";
    }

    /** Du Bois BSA formula: BSA = 0.007184 * w^0.425 * h^0.725 */
    public static Real bsaDuBois(Quantity<Mass> mass, Quantity<Length> height) {
        Real w = mass.to(Units.KILOGRAM).getValue();
        Real h = height.to(Units.CENTIMETER).getValue();
        return Real.of(0.007184)
                .multiply(Real.of(Math.pow(w.doubleValue(), 0.425)))
                .multiply(Real.of(Math.pow(h.doubleValue(), 0.725)));
    }

    /** Mosteller BSA: sqrt(weight * height / 3600) */
    public static Real bsaMosteller(Quantity<Mass> mass, Quantity<Length> height) {
        Real w = mass.to(Units.KILOGRAM).getValue();
        Real h = height.to(Units.CENTIMETER).getValue();
        return w.multiply(h).divide(Real.of(3600)).sqrt();
    }

    /** Ideal body weight (Devine formula, male) */
    public static Quantity<Mass> idealBodyWeightMale(Quantity<Length> height) {
        Real cm = height.to(Units.CENTIMETER).getValue();
        Real inches = cm.divide(Real.of(2.54));
        Real ibw = Real.of(50).add(Real.of(2.3).multiply(inches.subtract(Real.of(60))));
        return Quantities.create(ibw, Units.KILOGRAM);
    }

    /** Ideal body weight (Devine formula, female) */
    public static Quantity<Mass> idealBodyWeightFemale(Quantity<Length> height) {
        Real cm = height.to(Units.CENTIMETER).getValue();
        Real inches = cm.divide(Real.of(2.54));
        Real ibw = Real.of(45.5).add(Real.of(2.3).multiply(inches.subtract(Real.of(60))));
        return Quantities.create(ibw, Units.KILOGRAM);
    }
}
