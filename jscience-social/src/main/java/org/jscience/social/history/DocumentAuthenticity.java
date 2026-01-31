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

package org.jscience.social.history;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Analyzes document authenticity through linguistic anachronism detection.
 * Identifies terms in a text that post-date the claimed creation period of the document.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class DocumentAuthenticity {

    private DocumentAuthenticity() {
        // Prevent instantiation
    }

    /**
     * Identifies potential anachronisms in a text based on etymological data.
     * 
     * @param text the text to analyze
     * @param targetYear the claimed year of creation (CE)
     * @param etymologyData a map of words to their earliest known appearance year
     * @return a list of identified anachronistic words
     * @throws NullPointerException if any argument is null
     */
    public static List<String> findAnachronisms(String text, int targetYear, Map<String, Integer> etymologyData) {
        Objects.requireNonNull(text, "Text cannot be null");
        Objects.requireNonNull(etymologyData, "Etymology data cannot be null");
        
        List<String> found = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");
        
        for (String w : words) {
            String clean = w.replaceAll("[^a-z]", "");
            if (!clean.isEmpty()) {
                Integer earliestYear = etymologyData.get(clean);
                if (earliestYear != null && earliestYear > targetYear) {
                    found.add(clean);
                }
            }
        }
        return found;
    }
}

