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
import java.util.Objects;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a linguistic word, defined as a sequence of one or more morphemes 
 * that form a minimal meaningful unit of language.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Word implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Standard parts of speech (POS) categories.
     */
    public enum PartOfSpeech {
        NOUN, VERB, ADJECTIVE, ADVERB, PRONOUN, PREPOSITION,
        CONJUNCTION, INTERJECTION, ARTICLE, DETERMINER, UNKNOWN
    }

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Morpheme> morphemes = new ArrayList<>();

    @Attribute
    private PartOfSpeech partOfSpeech = PartOfSpeech.UNKNOWN;

    @Attribute
    private String lemma;

    @Attribute
    private String definition;

    /**
     * Creates a simple word consisting of a single morpheme.
     * 
     * @param language the language of the word
     * @param text the textual representation
     */
    public Word(Language language, String text) {
        Objects.requireNonNull(language, "Language cannot be null");
        this.morphemes.add(new Morpheme(language, text));
    }

    /**
     * Creates a complex word from a sequence of morphemes.
     * 
     * @param morphemes list of morphemes in order
     * @throws IllegalArgumentException if list is empty
     */
    public Word(List<Morpheme> morphemes) {
        if (morphemes == null || morphemes.isEmpty()) {
            throw new IllegalArgumentException("Morphemes cannot be null or empty");
        }
        this.morphemes.addAll(morphemes);
    }

    /**
     * @return the language of this word, derived from its constituent morphemes
     */
    public Language getLanguage() {
        return morphemes.isEmpty() ? null : morphemes.get(0).getLanguage();
    }

    public List<Morpheme> getMorphemes() {
        return Collections.unmodifiableList(morphemes);
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech pos) {
        this.partOfSpeech = pos;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * @return the full textual string of the word
     */
    public String getString() {
        StringBuilder sb = new StringBuilder();
        for (Morpheme m : morphemes) sb.append(m.getString());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word word)) return false;
        return Objects.equals(morphemes, word.morphemes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(morphemes);
    }

    @Override
    public String toString() {
        return getString();
    }
}

