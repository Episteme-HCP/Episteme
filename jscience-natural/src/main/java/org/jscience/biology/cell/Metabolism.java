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

package org.jscience.biology.cell;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Energy;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;

/**
 * Cellular metabolism models.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Metabolism {

    /** Gibbs free energy of ATP hydrolysis (kJ/mol) */
    public static final Real ATP_HYDROLYSIS_G = Real.of(-30.5);

    /**
     * Calculates energy released from ATP hydrolysis.
     * ATP + H2O -> ADP + Pi
     * 
     * @param moles Moles of ATP hydrolyzed
     * @return Energy released
     */
    public static Quantity<Energy> atpHydrolysisEnergy(Real moles) {
        Real energyKj = ATP_HYDROLYSIS_G.abs().multiply(moles);
        return Quantities.create(energyKj.multiply(Real.of(1000)).doubleValue(), Units.JOULE);
    }

    /**
     * Simplified Glycolysis net reaction energy.
     * Net yield: 2 ATP per Glucose.
     */
    public static Real glycolysisAtpYield(Real glucoseMoles) {
        return Real.TWO.multiply(glucoseMoles);
    }

    /**
     * Cellular Respiration (Aerobic) total ATP yield.
     * Theoretical max: ~38 ATP per Glucose (~30-32 practical).
     */
    public static Real aerobicRespirationAtpYield(Real glucoseMoles) {
        return Real.of(32).multiply(glucoseMoles);
    }

    /**
     * Energy charge of the cell.
     * EC = ([ATP] + 0.5[ADP]) / ([ATP] + [ADP] + [AMP])
     * Healthy cell: 0.8 - 0.95
     */
    public static Real energyCharge(Real atpConc, Real adpConc, Real ampConc) {
        Real numerator = atpConc.add(adpConc.multiply(Real.of(0.5)));
        Real denominator = atpConc.add(adpConc).add(ampConc);
        return numerator.divide(denominator);
    }
}


