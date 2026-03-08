/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media;

import org.episteme.core.media.audio.AudioAlgorithmProvider;
import org.episteme.core.media.audio.AudioBuffer;
import org.episteme.core.media.audio.AudioOp;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.Operation;

/**
 * Universal interface for audio backends (standard, scientific, creative).
 * <p>
 * Extends {@link ComputeBackend} to integrate with the standard backend discovery
 * system ({@link org.episteme.core.technical.backend.BackendDiscovery}).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface AudioBackend extends ComputeBackend, AudioAlgorithmProvider<AudioBuffer> {

    @Override
    default String getType() {
        return "audio";
    }

    @Override
    default ExecutionContext createContext() {
        return new ExecutionContext() {
            @Override
            public <R> R execute(Operation<R> operation) {
                return operation.compute(this);
            }

            @Override
            public void close() {}
        };
    }

    @Override
    default HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
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
    default int getPriority() {
        return 0;
    }

    @Override
    default void shutdown() {
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

    @Override
    default AudioBuffer apply(AudioBuffer audio, AudioOp<AudioBuffer> op) {
        return op.process(audio);
    }

    @Override
    default AudioBuffer createAudio(Object data, int channels, int sampleRate) {
        if (data instanceof double[] samples) {
            return new AudioBuffer(samples, channels, sampleRate);
        }
        throw new IllegalArgumentException("Unsupported audio data type: " + (data != null ? data.getClass().getName() : "null"));
    }
}
