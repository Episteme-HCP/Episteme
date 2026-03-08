/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media;

import org.episteme.core.media.video.VideoAlgorithmProvider;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.HardwareAccelerator;

/**
 * Universal interface for video backends (playback, frame grabbing, analysis).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface VideoBackend extends ComputeBackend, VideoAlgorithmProvider {

    @Override
    default String getType() {
        return "video";
    }

    @Override
    default ExecutionContext createContext() {
        return null;
    }

    @Override
    default HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    default String getName() {
        return getBackendName();
    }

    /**
     * Loads a video resource.
     * @param path File path or URL
     */
    void load(String path) throws Exception;

    void play();
    void pause();
    void stop();

    double getTime();
    double getDuration();

    /**
     * Grabs the current frame as an image.
     * @param <T> the image type
     * @return the current frame
     */
    <T> T grabFrame();

    /**
     * Returns the friendly name of this backend instance.
     */
    String getBackendName();

    @Override
    default String getId() {
        return getBackendName().toLowerCase().replace(" ", "-");
    }

    @Override
    default String getDescription() {
        return getBackendName() + " video backend";
    }

    @Override
    default int getPriority() {
        return 0;
    }

    @Override
    default boolean isAvailable() {
        return true;
    }

    @Override
    default void shutdown() {
    }


    @Override
    default Object createBackend() {
        return this;
    }
}
