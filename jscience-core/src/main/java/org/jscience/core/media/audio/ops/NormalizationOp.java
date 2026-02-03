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

package org.jscience.core.media.audio.ops;

import org.jscience.core.media.audio.AudioBuffer;
import org.jscience.core.media.audio.AudioOp;

/**
 * Normalizes an AudioBuffer to a target peak amplitude.
 */
public class NormalizationOp implements AudioOp<AudioBuffer> {
    private final double targetPeak;

    public NormalizationOp(double targetPeak) {
        this.targetPeak = targetPeak;
    }

    @Override
    public AudioBuffer process(AudioBuffer input) {
        double[] samples = input.getSamples();
        double max = 0;
        for (double s : samples) {
            double abs = Math.abs(s);
            if (abs > max) max = abs;
        }

        if (max == 0) return input;

        double multiplier = targetPeak / max;
        double[] result = new double[samples.length];
        for (int i = 0; i < samples.length; i++) {
            result[i] = samples[i] * multiplier;
        }
        return new AudioBuffer(result, input.getChannels(), input.getSampleRate());
    }
}
