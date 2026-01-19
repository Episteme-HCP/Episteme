/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media;

/**
 * Interface definition for a generic Audio Engine.
 * Allows switching between JavaSound, TarsosDSP, or other backends.
 */
public interface AudioEngine {
    
    /**
     * Loads an audio file from a path.
     * @param path Absolute path or URI to the audio file.
     * @throws Exception if loading fails.
     */
    void load(String path) throws Exception;

    /**
     * Starts or resumes playback.
     */
    void play();

    /**
     * Pauses playback.
     */
    void pause();

    /**
     * Stops playback and rewinds.
     */
    void stop();

    /**
     * Returns the current playback position in seconds.
     */
    double getTime();

    /**
     * Returns the total duration in seconds.
     */
    double getDuration();

    /**
     * Gets the Fast Fourier Transform (FFT) data for visualization.
     * @return array of magnitudes.
     */
    float[] getSpectrum();

    /**
     * Returns the name of the backend engine (e.g., "JavaSound", "TarsosDSP").
     */
    String getBackendName();
}
