/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.core.media.video;

import java.util.*;

/**
 * Detects hard cuts and scene changes in video sequences.
 */
public final class SceneTransitionDetector {

    private SceneTransitionDetector() {}

    public record Transition(int frameIndex, double intensity, String type) {}

    /**
     * Detects hard cuts using histogram differences.
     */
    public static List<Transition> detectCuts(List<float[][]> frames, double threshold) {
        List<Transition> transitions = new ArrayList<>();
        
        for (int i = 1; i < frames.size(); i++) {
            double diff = calculateHistogramDiff(frames.get(i-1), frames.get(i));
            if (diff > threshold) {
                transitions.add(new Transition(i, diff, "HARD_CUT"));
            }
        }
        return transitions;
    }

    private static double calculateHistogramDiff(float[][] f1, float[][] f2) {
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
                int bin = Math.min(255, Math.max(0, (int)(val * 255)));
                hist[bin]++;
            }
        }
        return hist;
    }

    /**
     * Estimates likelihood of a Fade-to-Black.
     */
    public static double fadeToBlackIntensity(float[][] frame) {
        double avg = 0;
        for (float[] row : frame) {
            for (float val : row) avg += val;
        }
        avg /= (frame.length * frame[0].length);
        return 1.0 - avg; // 1.0 is pure black
    }
}

