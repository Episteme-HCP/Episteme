/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.audio;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import java.util.List;

/**
 * Service Provider Interface for spectrogram calculations.
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public interface AudioSpectrogramProvider extends AlgorithmProvider {

    /**
     * Calculates the magnitude spectrum of a buffer.
     * 
     * @param buffer Input audio data.
     * @param window Window function to apply.
     * @return Magnitude spectrum (half size of buffer if buffer is power of 2).
     */
    double[] calculateSpectrum(double[] buffer, AudioSpectrogram.WindowFunction window);

    /**
     * Computes a full spectrogram (list of spectrums) for a long buffer.
     * 
     * @param audioData Full audio data.
     * @param windowSize Size of the analysis window.
     * @param overlap Overlap between consecutive windows.
     * @param window Window function to apply.
     * @return List of magnitude spectrums.
     */
    List<double[]> computeSpectrogram(double[] audioData, int windowSize, int overlap, AudioSpectrogram.WindowFunction window);

    @Override
    default String getAlgorithmType() {
        return "Audio Spectrogram";
    }
}
