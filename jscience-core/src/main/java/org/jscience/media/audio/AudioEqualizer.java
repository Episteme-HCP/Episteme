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

package org.jscience.media.audio;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Standard biquad filters for audio equalization.
 */
public final class AudioEqualizer {

    private AudioEqualizer() {}

    public enum FilterType {
        LOW_PASS, HIGH_PASS, BAND_PASS, NOTCH, PEAKING_EQ, LOW_SHELF, HIGH_SHELF
    }

    public static final class BiquadParameters {
        public double b0, b1, b2, a1, a2;
        
        public BiquadParameters(double b0, double b1, double b2, double a1, double a2) {
            this.b0 = b0; this.b1 = b1; this.b2 = b2;
            this.a1 = a1; this.a2 = a2;
        }
    }

    /**
     * Calculates filter coefficients based on Robert Bristow-Johnson's Audio EQ Cookbook.
     * 
     * @param type Filter type
     * @param freq Cutoff/Center frequency (Hz)
     * @param sampleRate Sample rate (e.g., 44100)
     * @param q Quality factor
     * @param gainDb Gain in decibels (for Peaking/Shelf filters)
     */
    public static BiquadParameters calculateCoefficients(FilterType type, double freq, 
            double sampleRate, double q, double gainDb) {
        
        double aa = Math.pow(10, gainDb / 40.0);
        double omega = 2 * Math.PI * freq / sampleRate;
        double sn = Math.sin(omega);
        double cs = Math.cos(omega);
        double alpha = sn / (2 * q);
        
        double b0, b1, b2, a0, a1, a2;
        
        switch (type) {
            case LOW_PASS -> {
                b0 = (1 - cs) / 2;
                b1 = 1 - cs;
                b2 = (1 - cs) / 2;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
            }
            case HIGH_PASS -> {
                b0 = (1 + cs) / 2;
                b1 = -(1 + cs);
                b2 = (1 + cs) / 2;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
            }
            case BAND_PASS -> {
                b0 = alpha;
                b1 = 0;
                b2 = -alpha;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
            }
            case NOTCH -> {
                b0 = 1;
                b1 = -2 * cs;
                b2 = 1;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
            }
            case PEAKING_EQ -> {
                b0 = 1 + alpha * aa;
                b1 = -2 * cs;
                b2 = 1 - alpha * aa;
                a0 = 1 + alpha / aa;
                a1 = -2 * cs;
                a2 = 1 - alpha / aa;
            }
            default -> throw new UnsupportedOperationException("Type not implemented yet: " + type);
        }
        
        // Normalize by a0
        return new BiquadParameters(b0/a0, b1/a0, b2/a0, a1/a0, a2/a0);
    }

    /**
     * Processes a buffer of audio samples using the given filter coefficients.
     * Uses Direct Form I implementation.
     */
    public static double[] process(double[] input, BiquadParameters p) {
        double[] output = new double[input.length];
        double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        
        for (int i = 0; i < input.length; i++) {
            double x0 = input[i];
            double y0 = p.b0 * x0 + p.b1 * x1 + p.b2 * x2 - p.a1 * y1 - p.a2 * y2;
            
            output[i] = y0;
            
            x2 = x1;
            x1 = x0;
            y2 = y1;
            y1 = y0;
        }
        
        return output;
    }

    /**
     * Calculates the magnitude response (frequency response) at a specific frequency.
     */
    public static Real magnitudeResponse(BiquadParameters p, double freq, double sampleRate) {
        double phi = 2 * Math.PI * freq / sampleRate;
        double cosPhi = Math.cos(phi);
        double cos2Phi = Math.cos(2 * phi);
        
        double num = p.b0 * p.b0 + p.b1 * p.b1 + p.b2 * p.b2 +
                    2 * (p.b0 * p.b1 + p.b1 * p.b2) * cosPhi +
                    2 * p.b0 * p.b2 * cos2Phi;
        
        double den = 1 + p.a1 * p.a1 + p.a2 * p.a2 +
                    2 * (p.a1 + p.a1 * p.a2) * cosPhi +
                    2 * p.a2 * cos2Phi;
        
        return Real.of(Math.sqrt(num / den));
    }
}
