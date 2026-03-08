/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.vision;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.technical.backend.Backend;

/**
 * Service Provider Interface (SPI) for computer vision algorithm providers.
 *
 * @param <T> the type of image object handled (e.g. BufferedImage, Mat, CLImage).
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface VisionAlgorithmProvider<T> extends AlgorithmProvider {

    @Override
    default String getName() {
        return getAlgorithmType();
    }

    /**
     * Applies a generic operation to an image.
     */
    T apply(T image, ImageOp<T> op);
    
    /**
     * Creates a new image from the given data.
     */
    T createImage(Object data, int width, int height);

    @Override
    default int getPriority() {
        return 0;
    }

    @Override
    default boolean isAvailable() {
        return true;
    }

    @Override
    default String getAlgorithmType() {
        return "Computer Vision";
    }

    @Override
    default void shutdown() {
        // Default no-op
    }
}
