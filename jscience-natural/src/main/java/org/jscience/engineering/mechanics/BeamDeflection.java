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

package org.jscience.engineering.mechanics;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Force;

/**
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
     * ÃŽÂ´_max = P * LÃ‚Â³ / (48 * E * I)
     * 
     * @param load            Point load (N)
     * @param length          Beam length (m)
     * @param elasticModulus  Young's modulus E (Pa)
     * @param momentOfInertia Second moment of area I (mÃ¢ÂÂ´)
     * @return Maximum deflection
     */
    public static Quantity<Length> simplySupported_CenterLoad(
            Quantity<Force> load, Quantity<Length> length,
            double elasticModulus, double momentOfInertia) {

        double P = load.to(Units.NEWTON).getValue().doubleValue();
        double L = length.to(Units.METER).getValue().doubleValue();

        double deflection = P * L * L * L / (48 * elasticModulus * momentOfInertia);
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Maximum deflection for cantilever beam with end load.
     * ÃŽÂ´_max = P * LÃ‚Â³ / (3 * E * I)
     */
    public static Quantity<Length> cantilever_EndLoad(
            Quantity<Force> load, Quantity<Length> length,
            double elasticModulus, double momentOfInertia) {

        double P = load.to(Units.NEWTON).getValue().doubleValue();
        double L = length.to(Units.METER).getValue().doubleValue();

        double deflection = P * L * L * L / (3 * elasticModulus * momentOfInertia);
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Simply supported beam with uniformly distributed load.
     * ÃŽÂ´_max = 5 * w * LÃ¢ÂÂ´ / (384 * E * I)
     * 
     * @param loadPerMeter Distributed load (N/m)
     */
    public static Quantity<Length> simplySupported_UniformLoad(
            double loadPerMeter, Quantity<Length> length,
            double elasticModulus, double momentOfInertia) {

        double L = length.to(Units.METER).getValue().doubleValue();
        double deflection = 5 * loadPerMeter * L * L * L * L / (384 * elasticModulus * momentOfInertia);
        return Quantities.create(deflection, Units.METER);
    }

    /**
     * Rectangle moment of inertia.
     * I = b * hÃ‚Â³ / 12
     */
    public static double rectangleMomentOfInertia(double width, double height) {
        return width * Math.pow(height, 3) / 12;
    }

    /**
     * Circular cross-section moment of inertia.
     * I = Ãâ‚¬ * rÃ¢ÂÂ´ / 4
     */
    public static double circleMomentOfInertia(double radius) {
        return Math.PI * Math.pow(radius, 4) / 4;
    }
}


