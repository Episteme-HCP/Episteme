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

package org.jscience.core.media.backends;

import org.jscience.core.media.AudioBackend;
import org.jscience.core.technical.algorithm.FFTProvider;
import org.jscience.core.technical.algorithm.fft.MulticoreFFTProvider;
import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Standard JavaSound implementation.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JavaSoundBackend implements AudioBackend {
    private Clip clip;
    private double[] audioData;
    private AudioFormat format;
    private final FFTProvider fftProvider = new MulticoreFFTProvider();
    
    @Override
    public void load(String path) throws Exception {
        File file = new File(path);
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(file)) {
            format = ais.getFormat();
            
            // Read all bytes to compute spectrum later
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ais.transferTo(baos);
            byte[] bytes = baos.toByteArray();
            
            // Convert to double samples (assuming 16-bit PCM for now, common for WAV)
            audioData = convertToDouble(bytes, format);
            
            // Re-open for playback
            try (AudioInputStream playbackStream = AudioSystem.getAudioInputStream(file)) {
                clip = AudioSystem.getClip();
                clip.open(playbackStream);
            }
        }
    }

    private double[] convertToDouble(byte[] bytes, AudioFormat format) {
        int sampleSizeInBits = format.getSampleSizeInBits();
        int channels = format.getChannels();
        boolean isBigEndian = format.isBigEndian();
        
        int bytesPerSample = sampleSizeInBits / 8;
        int n = bytes.length / (bytesPerSample * channels);
        double[] samples = new double[n];
        
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int c = 0; c < channels; c++) {
                if (sampleSizeInBits == 16) {
                    sum += buffer.getShort();
                } else if (sampleSizeInBits == 8) {
                    sum += buffer.get();
                }
            }
            samples[i] = sum / channels;
        }
        return samples;
    }

    @Override public void play() { if(clip!=null) clip.start(); }
    @Override public void pause() { if(clip!=null) clip.stop(); }
    @Override public void stop() { if(clip!=null) { clip.stop(); clip.setFramePosition(0); } }
    @Override public double getTime() { return (clip!=null) ? clip.getMicrosecondPosition() / 1_000_000.0 : 0; }
    @Override public double getDuration() { return (clip!=null) ? clip.getMicrosecondLength() / 1_000_000.0 : 0; }
    
    @Override
    public float[] getSpectrum() {
        if (clip == null || audioData == null || !clip.isRunning()) {
            return new float[128];
        }

        int framePos = clip.getFramePosition();
        int windowSize = 256; // Power of 2
        float[] spectrum = new float[128];
        
        if (framePos + windowSize > audioData.length) {
            return spectrum;
        }

        double[] real = new double[windowSize];
        double[] imag = new double[windowSize];
        System.arraycopy(audioData, framePos, real, 0, windowSize);
        
        // Windowing function (Hamming)
        for (int i = 0; i < windowSize; i++) {
            double window = 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (windowSize - 1));
            real[i] *= window;
        }
        
        double[][] result = fftProvider.transform(real, imag);
        double[] resReal = result[0];
        double[] resImag = result[1];
        
        // Magnitude spectrum (first half)
        for (int i = 0; i < 128; i++) {
            double mag = Math.sqrt(resReal[i] * resReal[i] + resImag[i] * resImag[i]) / windowSize;
            spectrum[i] = (float) mag;
        }
        
        return spectrum;
    }

    @Override public String getBackendName() { return "Standard JavaSound"; }
}

