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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a document or textual passage, composed of a sequence of sentences.
 * Provides analytical utilities for word count and linguistic density.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
@Persistent
public class Text implements Serializable {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Sentence> sentences = new ArrayList<>();

    @Attribute
    private String title;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Language language;

    @Attribute
    private String rawText;

    /**
     * Creates an empty Text instance.
     */
    public Text() {}

    /**
     * Creates a text from prepared sentences.
     * @param sentences list of sentences
     */
    public Text(List<Sentence> sentences) {
        if (sentences != null) this.sentences.addAll(sentences);
    }

    /**
     * Creates a text by parsing raw textual input into constituent sentences.
     * @param rawText the source text string
     */
    public Text(String rawText) {
        this.rawText = rawText;
        parseIntoSentences(rawText);
    }

    /**
     * Creates a text with a specified language.
     * @param rawText the source text string
     * @param language the document language
     */
    public Text(String rawText, Language language) {
        this(rawText);
        this.language = language;
    }

    private void parseIntoSentences(String text) {
        if (text == null || text.trim().isEmpty()) return;
        // Basic regex for sentence boundaries
        String[] parts = text.split("(?<=[.!?])\\s+");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                sentences.add(new Sentence(trimmed));
            }
        }
    }

    public List<Sentence> getSentences() {
        return Collections.unmodifiableList(sentences);
    }

    public void addSentence(Sentence sentence) {
        if (sentence != null) sentences.add(sentence);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * @return the total count of words in the text
     */
    public int getWordCount() {
        return sentences.stream().mapToInt(Sentence::getWordCount).sum();
    }

    /**
     * @return calculation of the average number of words per sentence
     */
    public double getAverageWordsPerSentence() {
        return sentences.isEmpty() ? 0 : (double) getWordCount() / sentences.size();
    }

    @Override
    public String toString() {
        if (rawText != null) return rawText;
        StringBuilder sb = new StringBuilder();
        for (Sentence s : sentences) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(s.toString());
        }
        return sb.toString();
    }
}
