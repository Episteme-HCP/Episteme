/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.media;

/**
 * Universal Interface for Audio Backends (Standard, Scientific, Creative).
 */
public interface AudioBackend {

    /**
     * Loads an audio resource.
     * @param path File path or URL
     */
    void load(String path) throws Exception;

    void play();
    void pause();
    void stop();

    double getTime();
    double getDuration();

    /**
     * Returns the current frequency spectrum.
     * @return Array of magnitudes (usually 128 or 256 bands)
     */
    float[] getSpectrum();

    /**
     * Returns the friendly name of this backend instance.
     */
    String getBackendName();
}
