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

package org.jscience.ui.viewers.physics.classical.waves;

/**
 * Primitive implementation of Spectrum Provider.
 * Uses simple double arrays and Math functions.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PrimitiveSpectrumProvider implements SpectrumProvider {

    private final int BANDS;
    private double[] spectrum;

    public PrimitiveSpectrumProvider(int bands) {
        this.BANDS = bands;
        this.spectrum = new double[BANDS];
    }

    private String pattern = "Voice";

    @Override
    public void setSourcePattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void update(double time, double sensitivity) {
        for (int i = 0; i < BANDS; i++) {
            double val = 0.0;

            if ("Sine".equals(pattern)) {
                // Moving sine wave peak
                double peakPos = (Math.sin(time * 0.5) + 1.0) * 0.5 * BANDS; // 0 to BANDS
                double dist = Math.abs(i - peakPos);
                val = Math.exp(-dist * dist * 0.5); // Gaussian
            } else if ("Noise".equals(pattern)) {
                val = Math.random();
            } else if ("Harmonics".equals(pattern)) {
                // 3 harmonics
                double t = time * 2.0;
                val = Math.sin(t + i * 0.5) > 0.8 ? 0.8 : 0.0;
                if (i % 5 == 0)
                    val += 0.5;
            } else { // Voice / Default
                double base = Math.sin(time + i * 0.2) * 0.5 + 0.5;
                double high = Math.sin(time * 2.5 + i * 0.5) * 0.3;
                double random = Math.random() * 0.2;
                val = base + high + random;
            }

            // Falloff for high frequencies
            double falloff = Math.exp(-i / (double) BANDS);

            spectrum[i] = val * falloff * sensitivity;
        }
    }

    @Override
    public double[] getSpectrum() {
        return spectrum;
    }

    @Override
    public String getName() {
        return "Primitive (double[])";
    }
}


