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

package org.episteme.core.media.audio.ops;

import org.episteme.core.media.audio.AudioBuffer;
import org.episteme.core.media.audio.AudioEqualizer;
import org.episteme.core.media.audio.AudioOp;

/**
 * Audio operation that applies a biquad filter (LowPass, HighPass, etc.).
 */
public class AudioEqualizerOp implements AudioOp<AudioBuffer> {
    private final AudioEqualizer.FilterType type;
    private final double freq;
    private final double q;
    private final double gainDb;

    public AudioEqualizerOp(AudioEqualizer.FilterType type, double freq, double q, double gainDb) {
        this.type = type;
        this.freq = freq;
        this.q = q;
        this.gainDb = gainDb;
    }

    @Override
    public AudioBuffer process(AudioBuffer input) {
        AudioEqualizer.BiquadParameters params = AudioEqualizer.calculateCoefficients(
            type, freq, input.getSampleRate(), q, gainDb
        );
        
        double[] samples = input.getSamples();
        double[] result = AudioEqualizer.process(samples, params);
        
        return new AudioBuffer(result, input.getChannels(), input.getSampleRate());
    }
}
