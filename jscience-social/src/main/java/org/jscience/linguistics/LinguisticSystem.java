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

package org.jscience.linguistics;

import org.jscience.linguistics.analysis.LinguisticAnalysis;
import org.jscience.linguistics.quantitative.QuantitativeLinguistics;
import java.util.Map;
import java.util.List;

/**
 * Unified entry point for the JScience Linguistics system.
 * This class provides high-level scientific services for researchers to analyze 
 * languages and corpora using statistical and structural methods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class LinguisticSystem {

    private LinguisticSystem() {}

    /**
     * Conducts a full statistical profile of a corpus.
     * Includes TTR, entropy, and vocabulary insights.
     */
    public record CorpusProfile(
        String name,
        long documentCount,
        long totalTokens,
        long vocabularySize,
        double lexicalDiversity,
        double entropy
    ) {}

    public static CorpusProfile profileCorpus(Corpus corpus) {
        Map<String, Long> frequencies = corpus.getWordFrequency();
        return new CorpusProfile(
            corpus.getName(),
            corpus.getDocumentCount(),
            corpus.getTotalTokens(),
            corpus.getVocabularySize(),
            QuantitativeLinguistics.calculateTTR(corpus.getVocabularySize(), corpus.getTotalTokens()),
            QuantitativeLinguistics.calculateEntropy(frequencies)
        );
    }

    /**
     * Analyzes the readability and complexity of a text.
     */
    public record TextMetricsBinary(
        double fleschScore,
        int wordCount,
        int averageSyllablesPerWord
    ) {}

    public static TextMetricsBinary analyzeText(String text) {
        List<String> tokens = LinguisticAnalysis.tokenize(text);
        double totalSyllables = tokens.stream().mapToInt(LinguisticAnalysis::countSyllables).sum();
        
        return new TextMetricsBinary(
            LinguisticAnalysis.fleschReadingEase(text),
            tokens.size(),
            tokens.isEmpty() ? 0 : (int)(totalSyllables / tokens.size())
        );
    }
}
