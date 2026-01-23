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

package org.jscience.sports;

import java.io.Serializable;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Extracts performance metrics from video-derived tracking data.
 * Includes models for Expected Goals (xG) and shot analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class VideoAnalysisMetrics {

    private VideoAnalysisMetrics() {}

    /** Detailed data for a specific scoring attempt. */
    public record Shot(double x, double y, double distance, boolean isHeader) implements Serializable {}

    /**
     * Calculates the Expected Goals (xG) probability for a shot.
     * Uses distance and angle components derived from field coordinates.
     * 
     * @param shot the shot to analyze
     * @return the xG value (probability 0.0 to 1.0)
     */
    public static Real calculateXG(Shot shot) {
        if (shot == null) return Real.ZERO;
        double baseLine = 0.1;
        double distFactor = Math.exp(-shot.distance() / 20.0);
        double angleFactor = Math.abs(shot.x() / 50.0);
        return Real.of(baseLine * distFactor * Math.max(0.1, (1.0 - angleFactor)));
    }
}
