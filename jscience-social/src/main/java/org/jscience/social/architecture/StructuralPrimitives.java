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

package org.jscience.social.architecture;

import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Area;
import org.jscience.core.measure.quantity.Force;
import org.jscience.core.measure.quantity.Length;

/**
 * Fundamental structural engineering calculations for architectural analysis. 
 * Provides basic primitives for stress evaluation and structural component 
 * behavior (e.g., arch thrust).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class StructuralPrimitives {

    private StructuralPrimitives() {}

    /**
     * Calculates the uniform mechanical stress (pressure) on a surface.
     * 
     * @param load the total force applied (Force)
     * @param area the area over which the force is distributed (Area)
     * @return the calculated stress as a Quantity
     */
    public static Quantity<?> calculateStress(Quantity<Force> load, Quantity<Area> area) {
        return load.divide(area);
    }

    /**
     * Calculates the horizontal thrust (H) of a simple parabolic arch 
     * supporting a uniform load.
     * Formula: H = (w * L^2) / (8 * h)
     * 
     * @param uniformLoad the uniform load per unit length (e.g., N/m)
     * @param span the horizontal span of the arch (Length)
     * @param height the vertical rise of the arch (Length)
     * @return the horizontal thrust as a Force quantity
     */
    public static Quantity<Force> calculateArchThrust(Quantity<?> uniformLoad, Quantity<Length> span, Quantity<Length> height) {
        double w = uniformLoad.getValue().doubleValue(); 
        double l = span.to(Units.METER).getValue().doubleValue();
        double h = height.to(Units.METER).getValue().doubleValue();
        
        if (h == 0) return Quantities.create(0, Units.NEWTON);
        
        double thrust = (w * l * l) / (8 * h);
        return Quantities.create(thrust, Units.NEWTON);
    }
}

