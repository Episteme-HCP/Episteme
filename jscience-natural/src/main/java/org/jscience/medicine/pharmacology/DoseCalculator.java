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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;

/**
 * Weight-based medication dose calculator.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DoseCalculator {

    private DoseCalculator() {
    }

    /** Weight-based dose: dose = dosePerKg * weight */
    public static Real weightBasedDose(Real dosePerKg, Quantity<Mass> weight) {
        Real kg = weight.to(Units.KILOGRAM).getValue();
        return dosePerKg.multiply(kg);
    }

    /** BSA-based dose: dose = dosePerM2 * BSA */
    public static Real bsaBasedDose(Real dosePerM2, Real bsa) {
        return dosePerM2.multiply(bsa);
    }

    /** IV drip rate (ml/hour) */
    public static Real ivDripRate(Real totalDoseMg, Real concentrationMgPerMl, Real infusionHours) {
        Real volumeNeeded = totalDoseMg.divide(concentrationMgPerMl);
        return volumeNeeded.divide(infusionHours);
    }

    /** Clark's rule: Child dose = (weight_lbs / 150) * adult_dose */
    public static Real clarksRule(Real adultDoseMg, Quantity<Mass> childWeight) {
        Real lbs = childWeight.to(Units.KILOGRAM).getValue().multiply(Real.of(2.205));
        return lbs.divide(Real.of(150)).multiply(adultDoseMg);
    }

    /** Young's rule: Child dose = (age / (age + 12)) * adult_dose */
    public static Real youngsRule(Real adultDoseMg, int ageYears) {
        return Real.of(ageYears).divide(Real.of(ageYears + 12)).multiply(adultDoseMg);
    }

    /** Creatinine clearance (Cockcroft-Gault) */
    public static Real creatinineClearance(int ageYears, Quantity<Mass> weight,
            Real serumCreatinineMgDl, boolean isFemale) {
        Real kg = weight.to(Units.KILOGRAM).getValue();
        Real result = Real.of(140 - ageYears).multiply(kg)
                .divide(Real.of(72).multiply(serumCreatinineMgDl));
        return isFemale ? result.multiply(Real.of(0.85)) : result;
    }
}


