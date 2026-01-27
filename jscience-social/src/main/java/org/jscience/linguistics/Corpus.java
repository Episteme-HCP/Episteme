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

import java.util.*;

/**
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Corpus {

    private final String name;
    private final List<String> documents = new ArrayList<>();

    public Corpus(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addDocument(String text) {
        if (text != null) documents.add(text);
    }

    /**
     * Calculates word frequency map across the entire corpus.
     */
    public Map<String, Long> getWordFrequency() {
        Map<String, Long> frequency = new LinkedHashMap<>();
        for (String doc : documents) {
            List<String> tokens = org.jscience.linguistics.analysis.LinguisticAnalysis.tokenize(doc);
            for (String word : tokens) {
                frequency.put(word, frequency.getOrDefault(word, 0L) + 1);
            }
        }
        // Sort by frequency descending
        return frequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public long getTotalTokens() {
        return documents.stream()
                .mapToLong(doc -> org.jscience.linguistics.analysis.LinguisticAnalysis.tokenize(doc).size())
                .sum();
    }

    public long getVocabularySize() {
        return documents.stream()
                .flatMap(doc -> org.jscience.linguistics.analysis.LinguisticAnalysis.tokenize(doc).stream())
                .distinct()
                .count();
    }

    public int getDocumentCount() {
        return documents.size();
    }
}


