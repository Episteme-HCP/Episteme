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

import org.episteme.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Basic video motion detection and frame analysis.
 */
public final class VideoAnalyzer {

    private VideoAnalyzer() {}

    public record MotionResult(
        double changeRatio, // 0-1
        int centerX,
        int centerY,
        boolean isMotionDetected
    ) {}

    /**
     * Detects motion between two frames using absolute difference.
     * 
     * @param prev Gray frame
     * @param curr Gray frame
     * @param threshold Sensitivity threshold (0-1)
     */
    public static MotionResult detectMotion(float[][] prev, float[][] curr, float threshold) {
        int width = prev.length;
        int height = prev[0].length;
        int changedPixels = 0;
        
        long sumX = 0, sumY = 0;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (Math.abs(curr[x][y] - prev[x][y]) > threshold) {
                    changedPixels++;
                    sumX += x;
                    sumY += y;
                }
            }
        }
        
        double ratio = (double) changedPixels / (width * height);
        boolean detected = ratio > 0.01; // 1% change threshold
        
        int cx = detected ? (int) (sumX / changedPixels) : 0;
        int cy = detected ? (int) (sumY / changedPixels) : 0;
        
        return new MotionResult(ratio, cx, cy, detected);
    }

    /**
     * Estimates Optical Flow magnitude (simplified).
     */
    public static Real averageFlowMagnitude(float[][] prev, float[][] curr) {
        int width = prev.length;
        int height = prev[0].length;
        double sum = 0;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                sum += Math.abs(curr[x][y] - prev[x][y]);
            }
        }
        
        return Real.of(sum / (width * height));
    }

    /**
     * Sample frames indices for a given duration and frame rate.
     */
    public static List<Integer> getSampleIndices(int totalFrames, int targetSampleCount) {
        List<Integer> indices = new ArrayList<>();
        if (targetSampleCount >= totalFrames) {
            for (int i = 0; i < totalFrames; i++) indices.add(i);
        } else {
            double step = (double) totalFrames / targetSampleCount;
            for (int i = 0; i < targetSampleCount; i++) {
                indices.add((int) (i * step));
            }
        }
        return indices;
    }
}

