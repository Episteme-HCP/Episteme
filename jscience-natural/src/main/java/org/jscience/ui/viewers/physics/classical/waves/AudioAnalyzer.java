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

package org.jscience.ui.viewers.physics.classical.waves;

import org.jscience.mathematics.analysis.transform.SignalFFT;
import org.jscience.physics.classical.waves.SpectrumAnalysisProvider;
import org.jscience.physics.classical.waves.PrimitiveSpectrumAnalysisProvider;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to load audio and perform FFT analysis.
 * <p>
 * Uses the internal JScience {@link SignalFFT} implementation.
 * </p>
 */
public class AudioAnalyzer {

    private final float sampleRate;
    private final double[] audioData;

    public AudioAnalyzer(File audioFile) throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = ais.getFormat();
        this.sampleRate = format.getSampleRate();

        // Load data
        byte[] bytes = ais.readAllBytes();
        this.audioData = extractSamples(bytes, format);
        ais.close();
    }

    public double[] getAudioData() {
        return audioData;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    private double[] extractSamples(byte[] bytes, AudioFormat format) {
        // Simple extraction for 16-bit PCM (assuming little endian usually)
        int sampleSize = format.getSampleSizeInBits();
        boolean bigEndian = format.isBigEndian();
        int channels = format.getChannels();
        
        // We only use the first channel (mono) for visualization
        int bytesPerSample = sampleSize / 8;
        int numSamples = bytes.length / (bytesPerSample * channels);
        double[] samples = new double[numSamples];

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < numSamples; i++) {
            double sample = 0;
            if (sampleSize == 16) {
                sample = buffer.getShort(i * bytesPerSample * channels); // Take first channel
                sample /= 32768.0; // Normalize 16-bit
            } else if (sampleSize == 8) {
                // 8-bit is usually unsigned
                sample = (buffer.get(i * bytesPerSample * channels) & 0xFF) - 128;
                sample /= 128.0;
            }
            samples[i] = sample;
        }
        return samples;
    }

    public List<double[]> computeSpectrogram(int windowSize, int overlap, SpectrumAnalysisProvider provider) {
        List<double[]> spectrogram = new ArrayList<>();
        
        int step = windowSize - overlap;
        double[] hanningWindow = createHanningWindow(windowSize);

        for (int i = 0; i < audioData.length - windowSize; i += step) {
            double[] windowedSamples = new double[windowSize];
            
            // Apply window function
            for (int j = 0; j < windowSize; j++) {
                windowedSamples[j] = audioData[i + j] * hanningWindow[j];
            }

            // Perform FFT and get magnitude using the Provider
            // Sensitivity 1.0 here as normalization is handled in renderer
            double[] bins = provider.computeSpectrum(windowedSamples, windowSize / 2, 1.0);
            spectrogram.add(bins);
        }
        return spectrogram;
    }

    public List<double[]> computeSpectrogram(int windowSize, int overlap) {
        return computeSpectrogram(windowSize, overlap, new PrimitiveSpectrumAnalysisProvider());
    }

    private double[] createHanningWindow(int size) {
        double[] window = new double[size];
        for (int i = 0; i < size; i++) {
            window[i] = 0.5 * (1 - Math.cos(2 * Math.PI * i / (size - 1)));
        }
        return window;
    }
}
