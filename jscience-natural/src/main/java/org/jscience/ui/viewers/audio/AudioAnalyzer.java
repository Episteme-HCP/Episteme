package org.jscience.ui.viewers.audio;

import org.jscience.mathematics.analysis.transform.SignalFFT;
import org.jscience.mathematics.numbers.real.Real;

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

    public List<double[]> computeSpectrogram(int windowSize, int overlap) {
        List<double[]> spectrogram = new ArrayList<>();
        
        int step = windowSize - overlap;
        double[] window = createHanningWindow(windowSize);

        // Pre-allocate Real arrays to reuse if possible, but FFT alters them in-place, 
        // so we need fresh ones or reset them each time.
        
        for (int i = 0; i < audioData.length - windowSize; i += step) {
            Real[] realPart = new Real[windowSize];
            Real[] imagPart = new Real[windowSize];
            
            // Apply window function and convert to Real
            for (int j = 0; j < windowSize; j++) {
                realPart[j] = Real.of(audioData[i + j] * window[j]);
                imagPart[j] = Real.ZERO;
            }

            // Perform FFT using internal JScience implementation
            // Modified in-place
            SignalFFT.fft(realPart, imagPart);
            
            // Extract magnitude (only first half needed for real data)
            double[] bins = new double[windowSize / 2];
            for (int k = 0; k < bins.length; k++) {
                double r = realPart[k].doubleValue();
                double im = imagPart[k].doubleValue();
                bins[k] = Math.sqrt(r * r + im * im);
            }
            spectrogram.add(bins);
        }
        return spectrogram;
    }

    private double[] createHanningWindow(int size) {
        double[] window = new double[size];
        for (int i = 0; i < size; i++) {
            window[i] = 0.5 * (1 - Math.cos(2 * Math.PI * i / (size - 1)));
        }
        return window;
    }
}
