/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.video;

import org.episteme.core.technical.algorithm.AlgorithmProvider;
import org.episteme.core.media.VideoBackend;


/**
 * Service Provider Interface (SPI) for video algorithm providers.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface VideoAlgorithmProvider extends AlgorithmProvider {

    @Override
    default String getName() {
        return getAlgorithmType();
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
    default String getAlgorithmType() {
        return "Video Engine";
    }

    @Override
    default void shutdown() {
        // Default no-op
    }

    /**
     * Detects motion between two frames.
     * 
     * @param prev Gray frame (float[][])
     * @param curr Gray frame (float[][])
     * @param threshold Sensitivity threshold
     * @return Motion result
     */
    default SceneTransitionDetector.Transition detectMotion(float[][] prev, float[][] curr, float threshold) {
        // Default implementation delegates to static utility
        return VideoAnalyzer.detectMotion(prev, curr, threshold);
    }

    /**
     * Detects scene transitions in a list of frames.
     * 
     * @param frames List of gray frames (float[][])
     * @param threshold Sensitivity threshold
     * @return List of transitions
     */
    default java.util.List<SceneTransitionDetector.Transition> detectTransitions(java.util.List<float[][]> frames, double threshold) {
        // Default implementation delegates to static utility
        return SceneTransitionDetector.detectCuts(frames, threshold);
    }
}
