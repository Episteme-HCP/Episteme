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

package org.jscience.ui.physics.waves;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.ArrayList;
import java.util.List;

/**
 * Object-based Spectrum Provider.
 * Uses Complex numbers and Vector types to simulate signal processing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ObjectSpectrumProvider implements SpectrumProvider {

    private final int BANDS;
    private Vector<Real> spectrum; // Storing as Real vector for result

    // We could mock "Complex" signal processing by generating a Complex vector
    // and taking magnitude.

    public ObjectSpectrumProvider(int bands) {
        this.BANDS = bands;
        // Initialize with zeros
    }

    private String pattern = "Voice";

    @Override
    public void setSourcePattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void update(double time, double sensitivity) {
        List<Real> magnitudes = new ArrayList<>(BANDS);

        Real rTime = Real.of(time);
        Real rSens = Real.of(sensitivity);

        for (int i = 0; i < BANDS; i++) {
            Real idx = Real.of(i);
            Real val;

            if ("Sine".equals(pattern)) {
                // Moving sine wave peak
                double peakPos = (Math.sin(time * 0.5) + 1.0) * 0.5 * BANDS;
                double dist = Math.abs(i - peakPos);
                val = Real.of(Math.exp(-dist * dist * 0.5));
            } else if ("Noise".equals(pattern)) {
                val = Real.of(Math.random());
            } else if ("Harmonics".equals(pattern)) {
                double t = time * 2.0;
                double hVal = Math.sin(t + i * 0.5) > 0.8 ? 0.8 : 0.0;
                if (i % 5 == 0)
                    hVal += 0.5;
                val = Real.of(hVal);
            } else {
                // Mock scientific signal processing for "Voice" / Default
                Real r02 = Real.of(0.2);
                Real r05 = Real.of(0.5);
                Real r25 = Real.of(2.5);
                Real r03 = Real.of(0.3);

                // Real Part Calculation: base = sin(t + i*0.2) * 0.5 + 0.5
                Real theta1 = rTime.add(idx.multiply(r02));
                Real base = theta1.sin().multiply(r05).add(r05);

                // Imaginary Part Calculation: high = sin(t*2.5 + i*0.5) * 0.3
                Real theta2 = rTime.multiply(r25).add(idx.multiply(r05));
                Real high = theta2.sin().multiply(r03);

                // Create Complex Signal and get Magnitude
                Complex signal = Complex.of(base, high);
                val = signal.abs().add(Real.of(Math.random() * 0.2));
            }

            // Falloff: exp(-i / BANDS)
            double dFalloff = Math.exp(-i / (double) BANDS);
            Real falloff = Real.of(dFalloff);

            // Final = val * falloff * sens
            magnitudes.add(val.multiply(falloff).multiply(rSens));
        }

        // Create immutable vector (Heavy allocation)
        this.spectrum = DenseVector.of(magnitudes, org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public double[] getSpectrum() {
        if (spectrum == null)
            return new double[BANDS];
        double[] arr = new double[BANDS];
        for (int i = 0; i < BANDS; i++) {
            arr[i] = spectrum.get(i).doubleValue();
        }
        return arr;
    }

    @Override
    public String getName() {
        return "Scientific (Complex Objects)";
    }
}
