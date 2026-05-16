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

package org.episteme.social.sports;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Analyzes fan engagement levels by aggregating attendance, social media visibility, 
 * and qualitative sentiment index.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class FanEngagementAnalyzer {

    private FanEngagementAnalyzer() {}

    /**
     * Calculates a composite engagement score for a team or event.
     * 
     * @param attendance     physical attendance count
     * @param socialMentions volume of digital mentions
     * @param sentiment      aggregated sentiment index (-1.0 to 1.0)
     * @return a Real number representing the engagement level
     */
    public static Real engagementScore(int attendance, int socialMentions, double sentiment) {
        // Attendance counts for 40%, digital reach for 30%, modified by sentiment
        double score = attendance * 0.4 + socialMentions * 0.3 * (1.0 + sentiment);
        return Real.of(score);
    }
}

