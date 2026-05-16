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

package org.episteme.core.media.audio;

/**
 * Dynamic range processor (Compressor/Limiter/Gate).
 */
public final class AudioDynamicProcessor {

    private AudioDynamicProcessor() {}

    public record CompressionSettings(
        double thresholdDb,
        double ratio,      // e.g., 4.0 for 4:1
        double attackMs,
        double releaseMs,
        double kneeDb,
        double makeupGainDb
    ) {}

    /**
     * Applies dynamic range compression to an audio buffer.
     */
    public static double[] compress(double[] input, CompressionSettings settings, double sampleRate) {
        double[] output = new double[input.length];
        double envelope = 0;
        
        double attackToken = Math.exp(-1.0 / (sampleRate * settings.attackMs() / 1000.0));
        double releaseToken = Math.exp(-1.0 / (sampleRate * settings.releaseMs() / 1000.0));
        
        for (int i = 0; i < input.length; i++) {
            double sample = input[i];
            double absSample = Math.abs(sample);
            
            // Envelope following
            if (absSample > envelope) {
                envelope = attackToken * envelope + (1.0 - attackToken) * absSample;
            } else {
                envelope = releaseToken * envelope + (1.0 - releaseToken) * absSample;
            }
            
            // Gain calculation
            double envelopeDb = amplitudeToDb(envelope);
            double gainDb = 0;
            
            if (envelopeDb > settings.thresholdDb()) {
                gainDb = (settings.thresholdDb() - envelopeDb) * (1.0 - 1.0 / settings.ratio());
            }
            
            double gain = dbToAmplitude(gainDb + settings.makeupGainDb());
            output[i] = sample * gain;
        }
        
        return output;
    }

    /**
     * Applies a hard limiter to prevent clipping.
     */
    public static double[] limit(double[] input, double ceilingDb) {
        double ceiling = dbToAmplitude(ceilingDb);
        double[] output = new double[input.length];
        
        for (int i = 0; i < input.length; i++) {
            output[i] = Math.max(-ceiling, Math.min(ceiling, input[i]));
        }
        
        return output;
    }

    /**
     * Simple noise gate.
     */
    public static double[] gate(double[] input, double thresholdDb, double attackMs, 
            double releaseMs, double sampleRate) {
        
        double[] output = new double[input.length];
        double threshold = dbToAmplitude(thresholdDb);
        double gain = 0;
        
        double attackStep = 1.0 / (sampleRate * attackMs / 1000.0);
        double releaseStep = 1.0 / (sampleRate * releaseMs / 1000.0);
        
        for (int i = 0; i < input.length; i++) {
            double sample = input[i];
            boolean open = Math.abs(sample) > threshold;
            
            if (open) gain = Math.min(1.0, gain + attackStep);
            else gain = Math.max(0.0, gain - releaseStep);
            
            output[i] = sample * gain;
        }
        
        return output;
    }

    public static double amplitudeToDb(double amp) {
        if (amp <= 0) return -144.0;
        return 20.0 * Math.log10(amp);
    }

    public static double dbToAmplitude(double db) {
        return Math.pow(10.0, db / 20.0);
    }
}

