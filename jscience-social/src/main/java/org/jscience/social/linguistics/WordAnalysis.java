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

package org.jscience.social.linguistics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores morphological and semantic analysis of a word.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class WordAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String originalText;
    private String lemma;
    private POS pos;
    private final Map<String, String> features = new HashMap<>();

    public WordAnalysis(String text) {
        this.originalText = text;
        this.pos = POS.UNKNOWN;
    }

    public String getOriginalText() { return originalText; }
    public String getLemma() { return lemma; }
    public void setLemma(String lemma) { this.lemma = lemma; }
    public POS getPOS() { return pos; }
    public void setPOS(POS pos) { this.pos = pos; }

    public void setFeature(String key, String value) {
        features.put(key, value);
    }

    public String getFeature(String key) {
        return features.get(key);
    }

    public Map<String, String> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] -> %s %s", originalText, lemma, pos, features);
    }
}

