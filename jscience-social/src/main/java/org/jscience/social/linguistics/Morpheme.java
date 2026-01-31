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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a morphemeâ€”the smallest irreducible unit of meaning or 
 * grammatical function within a language. Morphemes are the building 
 * blocks of words.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Morpheme implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Functional and structural classification of morphemes.
     */
    public enum Type {
        /** Can stand alone as a word (e.g., 'cat'). */
        FREE, 
        /** Must be attached to another morpheme (e.g., '-ing'). */
        BOUND, 
        /** Bound morpheme at the start of a word. */
        PREFIX, 
        /** Bound morpheme at the end of a word. */
        SUFFIX, 
        /** Bound morpheme inserted within a root. */
        INFIX, 
        /** The core semantic part of a word. */
        ROOT, 
        /** Classification unknown. */
        UNKNOWN
    }

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Language language;

    @Attribute
    private final List<Grapheme> graphemes = new ArrayList<>();

    @Attribute
    private final List<Phoneme> phonemes = new ArrayList<>();

    @Attribute
    private Type type = Type.UNKNOWN;

    @Attribute
    private String meaning;

    /**
     * Constructs a morpheme from a raw string.
     * 
     * @param language the language of the morpheme
     * @param text the orthographic representation
     */
    public Morpheme(Language language, String text) {
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        if (text != null) {
            for (char c : text.toCharArray()) {
                graphemes.add(new Grapheme(language, c));
            }
        }
    }

    /**
     * Constructs a morpheme from explicit phonetic and orthographic units.
     * 
     * @param language the language
     * @param graphemes list of constituent graphemes
     * @param phonemes list of constituent phonemes
     */
    public Morpheme(Language language, List<Grapheme> graphemes, List<Phoneme> phonemes) {
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        if (graphemes != null) this.graphemes.addAll(graphemes);
        if (phonemes != null) this.phonemes.addAll(phonemes);
    }

    public Language getLanguage() {
        return language;
    }

    /** @return unmodifiable list of internal graphemes */
    public List<Grapheme> getGraphemes() {
        return Collections.unmodifiableList(graphemes);
    }

    /** @return unmodifiable list of internal phonemes */
    public List<Phoneme> getPhonemes() {
        return Collections.unmodifiableList(phonemes);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type != null ? type : Type.UNKNOWN;
    }

    /** @return the semantic meaning or definition of the morpheme */
    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * @return the concatenated string of the morphemes's graphemes
     */
    public String getString() {
        if (!graphemes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Grapheme g : graphemes) sb.append(g.getCharacter());
            return sb.toString();
        }
        return "[Phonetic Morpheme]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Morpheme morpheme)) return false;
        return Objects.equals(language, morpheme.language) && 
               Objects.equals(getString(), morpheme.getString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, getString());
    }

    @Override
    public String toString() {
        return getString();
    }
}

