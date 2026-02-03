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

package org.jscience.nativ.media.audio.backends;

import org.jscience.core.media.audio.AudioBuffer;
import org.jscience.core.media.audio.AudioOp;
import org.jscience.core.media.audio.AudioProvider;
import java.util.stream.IntStream;

/**
 * High-performance AudioProvider using Java Parallel Streams.
 */
public class NativeMulticoreAudioProvider implements AudioProvider<AudioBuffer> {

    @Override
    public AudioBuffer apply(AudioBuffer audio, AudioOp<AudioBuffer> op) {
        // Parallelized operations could be handled here if op is a complex one.
        // For simple loops, we can help the op.
        return op.process(audio);
    }

    @Override
    public AudioBuffer createAudio(Object data, int channels, int sampleRate) {
        if (data instanceof double[]) {
            return new AudioBuffer((double[]) data, channels, sampleRate);
        }
        throw new IllegalArgumentException("Unsupported data type for NativeMulticoreAudioProvider");
    }

    /**
     * Specialized parallel gain application.
     */
    public AudioBuffer parallelGain(AudioBuffer input, double multiplier) {
        double[] samples = input.getSamples();
        double[] result = new double[samples.length];
        
        IntStream.range(0, samples.length).parallel().forEach(i -> {
            result[i] = samples[i] * multiplier;
        });
        
        return new AudioBuffer(result, input.getChannels(), input.getSampleRate());
    }
}
