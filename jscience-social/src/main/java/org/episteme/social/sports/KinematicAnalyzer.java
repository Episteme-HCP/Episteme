/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sports;

import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Mass;
import org.episteme.core.measure.quantity.Time;
import org.episteme.core.measure.quantity.Energy;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;

/**
 * Analyzes human kinematics and calculates metabolic energy expenditure for 
 * various sports activities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class KinematicAnalyzer {

    private KinematicAnalyzer() {}

    /**
     * Calculates the estimated energy expenditure using the Metabolic Equivalent of Task (MET) formula.
     * Energy (kcal) = MET Ã— mass (kg) Ã— duration (hours)
     * 
     * @param met      the activity MET value (e.g., 8.0 for running)
     * @param mass     weight of the athlete
     * @param duration time duration of the exertion
     * @return energy Expenditure in Joules
     */
    public static Quantity<Energy> calculateEnergyExpenditure(double met, Quantity<Mass> mass, Quantity<Time> duration) {
        if (mass == null || duration == null) return Quantities.create(0.0, Units.JOULE);
        
        double weightKg = mass.to(Units.KILOGRAM).getValue().doubleValue();
        double hours = duration.to(Units.HOUR).getValue().doubleValue();
        
        double kcal = met * weightKg * hours;
        // 1 kcal = 4184 Joules
        return Quantities.create(kcal * 4184.0, Units.JOULE);
    }
}

