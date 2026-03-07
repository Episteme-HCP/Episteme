/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.audio;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;

/**
 * Service Provider Interface for scientific audio processing backends.
 */
public interface AudioAlgorithmBackend<T> extends Backend, AlgorithmProvider {

    @Override
    default void shutdown() {}

    /**
     * Applies an operation to audio data.
     */
    T apply(T audio, AudioOp<T> op);

    /**
     * Creates an audio object from raw data.
     */
    T createAudio(Object data, int channels, int sampleRate);

    @Override
    default String getAlgorithmType() {
        return "Audio Processing";
    }

    @Override
    default int getPriority() {
        return 0;
    }

    @Override
    default boolean isAvailable() {
        return true;
    }
}
