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

package org.episteme.natural.engineering.mechanics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Force;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.measure.quantity.Pressure;

/**
 * Beam deflection calculations.
 * Modernized to use high-precision Real and typed Quantities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BeamDeflection {

    private BeamDeflection() {
    }

    /**
     * Maximum deflection for simply supported beam with center point load.
     * Î´_max = P * LÂ³ / (48 * E * I)
     * 
     * @param load            Point load (N)
     * @param length          Beam length (m)
     * @param elasticModulus  Young's modulus E
     * @param momentOfInertia Second moment of area I (mâ´)
     * @return Maximum deflection
     */
    public static Quantity<Length> simplySupported_CenterLoad(
            Quantity<Force> load, Quantity<Length> length,
            Quantity<Pressure> elasticModulus, Real momentOfInertia) {

        Real P = load.to(Units.NEWTON).getValue();
        Real L = length.to(Units.METER).getValue();
        Real E = elasticModulus.to(Units.PASCAL).getValue();
        Real I = momentOfInertia;

        Real deflection = P.multiply(L.pow(3)).divide(Real.of(48).multiply(E).multiply(I));
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Maximum deflection for cantilever beam with end load.
     * Î´_max = P * LÂ³ / (3 * E * I)
     */
    public static Quantity<Length> cantilever_EndLoad(
            Quantity<Force> load, Quantity<Length> length,
            Quantity<Pressure> elasticModulus, Real momentOfInertia) {

        Real P = load.to(Units.NEWTON).getValue();
        Real L = length.to(Units.METER).getValue();
        Real E = elasticModulus.to(Units.PASCAL).getValue();
        Real I = momentOfInertia;

        Real deflection = P.multiply(L.pow(3)).divide(Real.of(3).multiply(E).multiply(I));
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Simply supported beam with uniformly distributed load.
     * Î´_max = 5 * w * Lâ´ / (384 * E * I)
     * 
     * @param loadPerMeter Distributed load (N/m) - here passed as Force/Length usually, or just N/m Real. 
     *                     Using Force/Length is clearer, but for now we take Force (total?) or N/m?
     *                     Code implies w is N/m. Quantity<Force> divided by Quantity<Length> is ForcePerLength.
     *                     For simplicity, we take Real (N/m) or Quantity<Force> per meter? 
     *                     Let's use Quantity<Force> and divide by meter unit implicitly? No, w is load PER meter.
     *                     Let's use Real for loadVal (N/m).
     */
    public static Quantity<Length> simplySupported_UniformLoad(
            Real loadPerMeter, Quantity<Length> length,
            Quantity<Pressure> elasticModulus, Real momentOfInertia) {

        Real w = loadPerMeter;
        Real L = length.to(Units.METER).getValue();
        Real E = elasticModulus.to(Units.PASCAL).getValue();
        Real I = momentOfInertia;

        Real deflection = Real.of(5).multiply(w).multiply(L.pow(4))
                .divide(Real.of(384).multiply(E).multiply(I));
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Rectangle moment of inertia.
     * I = b * hÂ³ / 12
     */
    public static Real rectangleMomentOfInertia(Quantity<Length> width, Quantity<Length> height) {
        Real w = width.to(Units.METER).getValue();
        Real h = height.to(Units.METER).getValue();
        return w.multiply(h.pow(3)).divide(Real.of(12));
    }

    /**
     * Circular cross-section moment of inertia.
     * I = Ï€ * râ´ / 4
     */
    public static Real circleMomentOfInertia(Quantity<Length> radius) {
        Real r = radius.to(Units.METER).getValue();
        return Real.PI.multiply(r.pow(4)).divide(Real.of(4));
    }
}



