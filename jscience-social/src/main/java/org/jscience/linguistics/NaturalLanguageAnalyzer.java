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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Universal analyzer for Natural Languages.
 * Provides a framework for POS tagging, lemmatization and morphological analysis.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NaturalLanguageAnalyzer {

    private static final Map<String, NaturalLanguageAnalyzer> INSTANCES = new ConcurrentHashMap<>();

    private final Language language;


    protected NaturalLanguageAnalyzer(Language language) {
        this.language = language;
    }

    public static NaturalLanguageAnalyzer getInstance(Language language) {
        return INSTANCES.computeIfAbsent(language.getName(), k -> new NaturalLanguageAnalyzer(language));
    }

    public Language getLanguage() {
        return language;
    }

    /**
     * Performs analysis of a single word.
     * @param word the word text
     * @return the analysis result
     */
    public WordAnalysis analyzeWord(String word) {
        // Basic placeholder implementation
        WordAnalysis analysis = new WordAnalysis(word);
        analysis.setLemma(word.toLowerCase());
        
        // Language specific rules can be injected here
        applyLanguageRules(analysis);
        
        return analysis;
    }

    /**
     * Analyzes a full sentence.
     * @param sentence the sentence text
     * @return list of word analyses
     */
    public List<WordAnalysis> analyzeSentence(String sentence) {
        String[] tokens = sentence.split("\\s+");
        List<WordAnalysis> result = new ArrayList<>();
        for (String token : tokens) {
            result.add(analyzeWord(token.replaceAll("[^a-zA-ZáàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ]", "")));
        }
        return result;
    }

    protected void applyLanguageRules(WordAnalysis analysis) {
        String text = analysis.getOriginalText().toLowerCase();
        
        // Support for Latin/French basic markers
        if (language.getName().equalsIgnoreCase("Latin")) {
            if (text.endsWith("us") || text.endsWith("a") || text.endsWith("um")) {
                analysis.setPOS(POS.NOUN);
                analysis.setFeature("case", "nominative");
            } else if (text.endsWith("re")) {
                analysis.setPOS(POS.VERB);
                analysis.setFeature("mood", "infinitive");
            }
        } else if (language.getName().equalsIgnoreCase("French")) {
            if (text.startsWith("l'") || text.equals("le") || text.equals("la") || text.equals("les")) {
                analysis.setPOS(POS.DETERMINER);
            } else if (text.endsWith("er") || text.endsWith("ir") || text.endsWith("re")) {
                analysis.setPOS(POS.VERB);
            }
        }
    }
}
