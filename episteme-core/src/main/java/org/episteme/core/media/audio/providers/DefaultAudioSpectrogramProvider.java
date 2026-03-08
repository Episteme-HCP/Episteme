/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.audio.providers;

import com.google.auto.service.AutoService;
import org.episteme.core.media.audio.AudioSpectrogram;
import org.episteme.core.media.audio.AudioSpectrogramProvider;
import org.episteme.core.mathematics.analysis.fft.FFTProvider;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.mathematics.analysis.fft.providers.MulticoreFFTProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link AudioSpectrogramProvider} using {@link FFTProvider}.
 * 
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(AudioSpectrogramProvider.class)
public class DefaultAudioSpectrogramProvider implements AudioSpectrogramProvider {

    @Override
    public String getName() {
        return "Default FFT Spectrogram";
    }

    @Override
    public double[] calculateSpectrum(double[] buffer, AudioSpectrogram.WindowFunction window) {
        int n = buffer.length;
        // FFT requires power of 2
        if ((n & (n - 1)) != 0) {
            int powerOfTwo = 1 << (31 - Integer.numberOfLeadingZeros(n));
            double[] truncated = new double[powerOfTwo];
            System.arraycopy(buffer, 0, truncated, 0, powerOfTwo);
            buffer = truncated;
            n = powerOfTwo;
        }
        
        double[] real = buffer.clone();
        applyWindow(real, window);
        double[] imag = new double[n];

        FFTProvider provider = AlgorithmManager.getProvider(FFTProvider.class);
        if (provider == null) provider = new MulticoreFFTProvider();
        
        double[][] result = provider.transform(real, imag);
        
        double[] rReal = result[0];
        double[] rImag = result[1];
        
        double[] magnitude = new double[n / 2];
        for (int i = 0; i < n / 2; i++) {
            magnitude[i] = Math.sqrt(rReal[i] * rReal[i] + rImag[i] * rImag[i]);
        }
        
        return magnitude;
    }

    @Override
    public List<double[]> computeSpectrogram(double[] audioData, int windowSize, int overlap, AudioSpectrogram.WindowFunction window) {
        List<double[]> spectrogram = new ArrayList<>();
        int step = windowSize - overlap;

        for (int i = 0; i < audioData.length - windowSize; i += step) {
            double[] chunk = new double[windowSize];
            System.arraycopy(audioData, i, chunk, 0, windowSize);
            spectrogram.add(calculateSpectrum(chunk, window));
        }
        return spectrogram;
    }

    private void applyWindow(double[] data, AudioSpectrogram.WindowFunction type) {
        int n = data.length;
        for (int i = 0; i < n; i++) {
            double factor = switch (type) {
                case RECTANGULAR -> 1.0;
                case HANNING -> 0.5 * (1 - Math.cos(2 * Math.PI * i / (n - 1)));
                case HAMMING -> 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (n - 1));
                case BLACKMAN -> 0.42 - 0.5 * Math.cos(2 * Math.PI * i / (n - 1)) + 0.08 * Math.cos(4 * Math.PI * i / (n - 1));
            };
            data[i] *= factor;
        }
    }
}
