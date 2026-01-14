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

package org.jscience.chemistry.spectroscopy;

import org.jscience.mathematics.numbers.real.Real;

/**
 * IR spectroscopy functional group frequencies.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class IRSpectrum {

    public enum FunctionalGroup {
        OH_ALCOHOL("O-H (alcohol)", 3200, 3550, "broad"),
        OH_CARBOXYLIC("O-H (carboxylic acid)", 2500, 3300, "very broad"),
        NH_AMINE("N-H (amine)", 3300, 3500, "medium"),
        CH_ALKANE("C-H (spÃ‚Â³)", 2850, 3000, "strong"),
        CH_ALKENE("C-H (spÃ‚Â²)", 3000, 3100, "medium"),
        CH_ALKYNE("C-H (sp)", 3300, 3300, "strong, sharp"),
        CH_ALDEHYDE("C-H (aldehyde)", 2700, 2850, "two peaks"),
        CC_TRIPLE("CÃ¢â€°Â¡C", 2100, 2260, "weak"),
        CN_TRIPLE("CÃ¢â€°Â¡N (nitrile)", 2210, 2260, "medium"),
        CC_DOUBLE("C=C", 1620, 1680, "variable"),
        CO_DOUBLE_KETONE("C=O (ketone)", 1705, 1725, "strong"),
        CO_DOUBLE_ALDEHYDE("C=O (aldehyde)", 1720, 1740, "strong"),
        CO_DOUBLE_ESTER("C=O (ester)", 1735, 1750, "strong"),
        CO_DOUBLE_CARBOXYLIC("C=O (carboxylic acid)", 1700, 1725, "strong"),
        CO_DOUBLE_AMIDE("C=O (amide)", 1640, 1690, "strong"),
        CO_ETHER("C-O (ether)", 1000, 1300, "strong"),
        CN_AMINE("C-N (amine)", 1020, 1250, "medium"),
        NO2_NITRO("N-O (nitro)", 1515, 1560, "strong");

        private final String name;
        private final int wavenumberLow;
        private final int wavenumberHigh;
        private final String intensity;

        FunctionalGroup(String name, int low, int high, String intensity) {
            this.name = name;
            this.wavenumberLow = low;
            this.wavenumberHigh = high;
            this.intensity = intensity;
        }

        public String getName() {
            return name;
        }

        public int getWavenumberLow() {
            return wavenumberLow;
        }

        public int getWavenumberHigh() {
            return wavenumberHigh;
        }

        public String getIntensity() {
            return intensity;
        }

        public boolean matches(Real wavenumber) {
            double wn = wavenumber.doubleValue();
            return wn >= wavenumberLow && wn <= wavenumberHigh;
        }
    }

    /** Identify functional groups from peak wavenumber */
    public static java.util.List<FunctionalGroup> identifyPeak(Real wavenumber) {
        java.util.List<FunctionalGroup> matches = new java.util.ArrayList<>();
        for (FunctionalGroup fg : FunctionalGroup.values()) {
            if (fg.matches(wavenumber))
                matches.add(fg);
        }
        return matches;
    }

    /** Wavelength (ÃŽÂ¼m) to wavenumber (cmÃ¢ÂÂ»Ã‚Â¹): ÃŽÂ½ÃŒÆ’ = 10000 / ÃŽÂ» */
    public static Real wavelengthToWavenumber(Real wavelengthMicrons) {
        return Real.of(10000).divide(wavelengthMicrons);
    }

    /** Wavenumber to wavelength */
    public static Real wavenumberToWavelength(Real wavenumber) {
        return Real.of(10000).divide(wavenumber);
    }

    /** Stretching frequency: ÃŽÂ½ÃŒÆ’ = (1/2Ãâ‚¬c) Ã‚Â· Ã¢Ë†Å¡(k/ÃŽÂ¼) */
    public static Real stretchingFrequency(Real k, Real mu) {
        Real c = Real.of(2.998e10);
        return Real.ONE.divide(Real.TWO_PI.multiply(c))
                .multiply(k.divide(mu).sqrt());
    }
}


