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

package org.jscience.earth.oceanography;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Tidal prediction using harmonic analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TidalPrediction {

    private TidalPrediction() {
    }

    public static class TidalConstituent {
        public final String name;
        public final Real amplitude;
        public final Real phase;
        public final Real speed;

        public TidalConstituent(String name, Real amplitude, Real phase, Real speed) {
            this.name = name;
            this.amplitude = amplitude;
            this.phase = phase;
            this.speed = speed;
        }

        public TidalConstituent(String name, double amplitude, double phase, double speed) {
            this(name, Real.of(amplitude), Real.of(phase), Real.of(speed));
        }
    }

    public static final Real M2_SPEED = Real.of(28.984104);
    public static final Real S2_SPEED = Real.of(30.0);
    public static final Real N2_SPEED = Real.of(28.439730);
    public static final Real K1_SPEED = Real.of(15.041069);
    public static final Real O1_SPEED = Real.of(13.943035);
    public static final Real M4_SPEED = Real.of(57.968208);

    /** Predict height: h(t) = hÃ¢â€šâ‚¬ + ÃŽÂ£ AÃ¡ÂµÂ¢ cos(Ãâ€°Ã¡ÂµÂ¢t - Ãâ€ Ã¡ÂµÂ¢) */
    public static Real predictHeight(Real meanSeaLevel, TidalConstituent[] constituents, Real hoursFromEpoch) {
        Real height = meanSeaLevel;
        for (TidalConstituent c : constituents) {
            Real angle = c.speed.multiply(hoursFromEpoch).subtract(c.phase);
            Real cosAngle = angle.toRadians().cos();
            height = height.add(c.amplitude.multiply(cosAngle));
        }
        return height;
    }

    /** Max tidal range: 2 * sum of amplitudes */
    public static Real maxTidalRange(TidalConstituent[] constituents) {
        Real sum = Real.ZERO;
        for (TidalConstituent c : constituents) {
            sum = sum.add(c.amplitude);
        }
        return Real.TWO.multiply(sum);
    }

    /** Simple M2+S2 model */
    public static TidalConstituent[] simpleSemidiurnalModel(Real m2Amp, Real m2Phase, Real s2Amp, Real s2Phase) {
        return new TidalConstituent[] {
                new TidalConstituent("M2", m2Amp, m2Phase, M2_SPEED),
                new TidalConstituent("S2", s2Amp, s2Phase, S2_SPEED)
        };
    }

    /** Hours to next high tide */
    public static Real hoursToNextHighTide(Real currentHoursFromEpoch) {
        Real phase = currentHoursFromEpoch.multiply(M2_SPEED).remainder(Real.of(360));
        return Real.of(360).subtract(phase).divide(M2_SPEED);
    }
}


