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
import java.util.List;

/**
 * Utility for detecting scene transitions in video streams.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
public final class SceneTransitionDetector {

    private SceneTransitionDetector() {}

    public record Transition(int frameIndex, double intensity, String type) {}

    /**
     * Detects hard cuts using histogram differences.
     */
    public static List<Transition> detectCuts(List<float[][]> frames, double threshold) {
        return VideoBackendSystem.getVideoBackend().detectTransitions(frames, threshold);
    }

    public static double calculateHistogramDiff(float[][] f1, float[][] f2) {
        int[] h1 = buildHistogram(f1);
        int[] h2 = buildHistogram(f2);
        
        double sum = 0;
        for (int i = 0; i < h1.length; i++) {
            sum += Math.abs(h1[i] - h2[i]);
        }
        
        return sum / (f1.length * f1[0].length);
    }

    private static int[] buildHistogram(float[][] frame) {
        int[] hist = new int[256];
        for (float[] row : frame) {
            for (float val : row) {
                int bin = (int) (val * 255);
                hist[Math.max(0, Math.min(255, bin))]++;
            }
        }
        return hist;
    }

    public static double fadeToBlackIntensity(float[][] frame) {
        double sum = 0;
        int w = frame.length;
        int h = frame[0].length;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                sum += frame[x][y];
            }
        }
        return 1.0 - (sum / (w * h));
    }
}
