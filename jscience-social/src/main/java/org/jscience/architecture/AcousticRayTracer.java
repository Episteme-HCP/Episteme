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

package org.jscience.architecture;

import java.io.Serializable;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Simplified acoustic simulation tool for modeling sound propagation and 
 * reverberation in architectural spaces using ray tracing concepts.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class AcousticRayTracer {

    private AcousticRayTracer() {}

    /**
     * Represents a single sound ray in terms of geometry and energy.
     */
    public record Ray(double[] origin, double[] direction, double intensity) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Detail of a sound reflection off a surface.
     */
    public record Reflection(double[] position, double absorptionCoeff) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Estimates sound attenuation due to air absorption over a specific distance.
     * Uses the formula: I = I0 * exp(-alpha * d)
     * 
     * @param distance distance traveled in meters
     * @param alpha absorption coefficient of air (varies with humidity/temp)
     * @return attenuation factor (0 to 1)
     */
    public static Real airAttenuation(double distance, double alpha) {
        return Real.of(Math.exp(-alpha * distance));
    }

    /**
     * Calculates the Sabine Reverberation Time (RT60), which is the time 
     * required for sound to decay by 60 decibels.
     * Uses the formula: RT60 = 0.161 * V / (S * alpha_avg)
     * 
     * @param volume total room volume in cubic meters
     * @param surfaceArea total internal surface area in square meters
     * @param avgAbsorption average absorption coefficient of all surfaces
     * @return estimated RT60 in seconds
     */
    public static Real calculateSabineRT60(double volume, double surfaceArea, double avgAbsorption) {
        if (surfaceArea * avgAbsorption == 0) return Real.ZERO;
        return Real.of(0.161 * volume / (surfaceArea * avgAbsorption));
    }
}
