/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.core.media.video;

import org.episteme.core.media.VideoBackendSystem;
import org.episteme.core.mathematics.numbers.real.Real;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility for video analysis tasks (motion detection, frame sampling, etc).
 */
public final class VideoAnalyzer {

    private VideoAnalyzer() {}

    public record MotionResult(boolean isMotionDetected, double changeRatio) {}
    
    // Alias for transition to avoid breaking some API consumers, but moving towards SPI
    public static SceneTransitionDetector.Transition detectMotion(float[][] prev, float[][] curr, float threshold) {
        return VideoBackendSystem.getVideoBackend().detectMotion(prev, curr, threshold);
    }
    
    /**
     * Reference implementation of motion detection.
     */
    public static MotionResult detectMotionReference(float[][] prev, float[][] curr, float threshold) {
        int width = prev.length;
        int height = prev[0].length;
        long changedPixels = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Math.abs(curr[x][y] - prev[x][y]) > threshold) {
                    changedPixels++;
                }
            }
        }

        double ratio = (double) changedPixels / (width * height);
        return new MotionResult(ratio > 0.05, ratio);
    }

    public static List<Integer> getSampleIndices(int totalFrames, int samples) {
        List<Integer> indices = new ArrayList<>();
        if (totalFrames <= 0 || samples <= 0) return indices;
        
        double step = (double) totalFrames / samples;
        for (int i = 0; i < samples; i++) {
            indices.add((int) (i * step));
        }
        return indices;
    }
}
