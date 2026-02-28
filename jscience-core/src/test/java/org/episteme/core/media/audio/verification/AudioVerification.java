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

package org.episteme.core.media.audio.verification;

import org.episteme.core.media.audio.AudioBuffer;
import org.episteme.core.media.audio.AudioEqualizer;
import org.episteme.core.media.audio.AudioAlgorithmBackend;
import org.episteme.core.media.audio.backends.JavaSoundBackend;
import org.episteme.core.media.audio.ops.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification tests for audio operations.
 */
public class AudioVerification {

    @Test
    public void testAudioOps() {
        AudioAlgorithmBackend<AudioBuffer> provider = new JavaSoundBackend();
        
        // Create a 1-second sine wave at 440Hz (mono, 44100Hz)
        int sampleRate = 44100;
        double[] samples = new double[sampleRate];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = Math.sin(2 * Math.PI * 440 * i / sampleRate);
        }
        
        AudioBuffer buffer = provider.createAudio(samples, 1, sampleRate);
        
        // Test Gain
        AudioBuffer gained = provider.apply(buffer, new GainOp(0.5));
        // At index 25, sin(2*PI*440*25/44100) is approx 1.0
        assertEquals(0.5, gained.getSamples()[25], 0.1);
        
        // Test Invert
        AudioBuffer inverted = provider.apply(buffer, new InvertOp());
        assertEquals(-buffer.getSamples()[0], inverted.getSamples()[0], 0.0001);
        
        // Test Normalization
        AudioBuffer normalized = provider.apply(gained, new NormalizationOp(1.0));
        double max = 0;
        for (double s : normalized.getSamples()) max = Math.max(max, Math.abs(s));
        assertEquals(1.0, max, 0.01);
        
        // Test Mono (already mono, but check)
        AudioBuffer mono = provider.apply(buffer, new MonoOp());
        assertEquals(1, mono.getChannels());
        
        // Test Equalizer (LowPass)
        AudioBuffer filtered = provider.apply(buffer, new AudioEqualizerOp(AudioEqualizer.FilterType.LOW_PASS, 100, 0.707, 0));
        assertNotNull(filtered);
        assertTrue(filtered.getSamples().length > 0);
    }
}
