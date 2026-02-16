/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.media;

import org.jscience.core.technical.backend.Backend;

/**
 * Universal interface for audio backends (standard, scientific, creative).
 * <p>
 * Extends {@link Backend} to integrate with the standard backend discovery
 * system ({@link org.jscience.core.technical.backend.BackendDiscovery}).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface AudioBackend extends Backend {

    @Override
    default String getType() {
        return "audio";
    }

    @Override
    default String getId() {
        return getBackendName().toLowerCase().replace(" ", "-");
    }

    @Override
    default String getName() {
        return getBackendName();
    }

    @Override
    default String getDescription() {
        return "Audio backend: " + getBackendName();
    }

    @Override
    default boolean isAvailable() {
        return true;
    }

    @Override
    default Object createBackend() {
        return this;
    }

    // ---- Audio-specific operations ----

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
