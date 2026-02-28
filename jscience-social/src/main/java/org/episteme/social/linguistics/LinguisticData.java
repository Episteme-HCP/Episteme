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

package org.episteme.social.linguistics;

import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.util.UniversalDataModel;
import java.util.*;

/**
 * Universal data model for linguistic analysis.
 * Supports syntax trees, word frequency (Zipf), and sentiment timelines.
 */
public final class LinguisticData implements UniversalDataModel {

    @Override
    public String getModelType() { return "LINGUISTIC_METRICS"; }

    public record SyntaxNode(String label, String word, List<SyntaxNode> children) {
        public SyntaxNode(String label, String word) { this(label, word, new ArrayList<>()); }
    }

    public record SentimentPoint(double time, Real score, String context) {}

    private SyntaxNode root;
    private final Map<String, Long> wordFrequencies = new LinkedHashMap<>();
    private final List<SentimentPoint> sentimentTimeline = new ArrayList<>();

    public void setRootNode(SyntaxNode root) { this.root = root; }
    public SyntaxNode getRootNode() { return root; }

    public void addWordFrequency(String word, long count) {
        wordFrequencies.put(word, count);
    }

    public Map<String, Long> getWordFrequencies() { return Collections.unmodifiableMap(wordFrequencies); }

    public void addSentiment(double time, Real score, String context) {
        sentimentTimeline.add(new SentimentPoint(time, score, context));
    }

    public List<SentimentPoint> getSentimentTimeline() { return Collections.unmodifiableList(sentimentTimeline); }
}

