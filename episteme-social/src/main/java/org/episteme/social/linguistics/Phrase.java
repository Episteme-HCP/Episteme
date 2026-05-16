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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a phraseâ€”a group of words that function together as a single 
 * syntactic unit within a sentence but do not necessarily contain a subject 
 * and a predicate (clause).
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Phrase implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Functional categories of phrases based on their head word.
     */
    public enum Type {
        /** Governed by a noun (e.g., 'the big red cat'). */
        NOUN_PHRASE, 
        /** Governed by a verb (e.g., 'carefully runs'). */
        VERB_PHRASE, 
        /** Governed by an adjective (e.g., 'extremely happy'). */
        ADJECTIVE_PHRASE, 
        /** Governed by an adverb (e.g., 'quite quickly'). */
        ADVERB_PHRASE, 
        /** Governed by a preposition (e.g., 'under the bridge'). */
        PREPOSITIONAL_PHRASE, 
        /** A syntactic structure containing a subject and a predicate. */
        CLAUSE, 
        /** Classification unknown. */
        UNKNOWN
    }

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Word> words = new ArrayList<>();

    @Attribute
    private Type type = Type.UNKNOWN;

    @Attribute
    private String rawText;

    /**
     * Initializes an empty phrase.
     */
    public Phrase() {}

    /**
     * Initializes a phrase from an ordered list of words.
     * @param words constituent words
     */
    public Phrase(List<Word> words) {
        if (words != null) this.words.addAll(words);
    }

    /**
     * Initializes a phrase by parsing a raw text string.
     * 
     * @param text the textual phrase
     * @param language the language for the constituent words
     */
    public Phrase(String text, Language language) {
        this.rawText = text;
        if (text != null && language != null) {
            String[] wordStrings = text.split("\\s+");
            for (String w : wordStrings) {
                if (!w.trim().isEmpty()) {
                    words.add(new Word(language, w));
                }
            }
        }
    }

    /** @return unmodifiable list of the phrase's constituent words */
    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }

    /**
     * Appends a word to the end of the phrase.
     * @param word word to add
     */
    public void addWord(Word word) {
        if (word != null) words.add(word);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type != null ? type : Type.UNKNOWN;
    }

    /** @return total count of words in the phrase */
    public int getWordCount() {
        return words.size();
    }

    /** 
     * @return the primary 'head' word of the phrase (simplified as the last word) 
     */
    public Word getHead() {
        return words.isEmpty() ? null : words.get(words.size() - 1);
    }

    @Override
    public String toString() {
        if (rawText != null) return rawText;
        StringBuilder sb = new StringBuilder();
        for (Word w : words) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(w.toString());
        }
        return sb.toString();
    }
}

