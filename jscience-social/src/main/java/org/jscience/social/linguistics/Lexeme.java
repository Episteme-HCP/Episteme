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
 * Represents a lexemeâ€”an abstract unit of the lexicon in a language that 
 * encompasses all inflected forms of a single word. For example, the lexeme 
 * 'run' includes the forms 'run', 'runs', 'ran', and 'running'.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Lexeme implements Serializable {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Word> forms = new ArrayList<>();

    @Attribute
    private String canonicalForm;

    @Attribute
    private Word.PartOfSpeech partOfSpeech = Word.PartOfSpeech.UNKNOWN;

    @Attribute
    private String definition;

    /**
     * Constructs a lexeme with a canonical form.
     * 
     * @param canonicalForm the dictionary form (lemma) of the lexeme
     * @param language the language of the lexeme
     */
    public Lexeme(String canonicalForm, Language language) {
        this.canonicalForm = Objects.requireNonNull(canonicalForm, "Canonical form cannot be null");
        Objects.requireNonNull(language, "Language cannot be null");
        this.forms.add(new Word(language, canonicalForm));
    }

    /**
     * Constructs a lexeme from an existing set of word forms.
     * 
     * @param forms list of word forms belonging to this lexeme
     * @throws IllegalArgumentException if forms is null or empty
     */
    public Lexeme(List<Word> forms) {
        if (forms == null || forms.isEmpty()) {
            throw new IllegalArgumentException("Forms cannot be null or empty");
        }
        this.forms.addAll(forms);
        this.canonicalForm = forms.get(0).getString();
    }

    /** @return the primary language associated with this lexeme */
    public Language getLanguage() {
        return forms.isEmpty() ? null : forms.get(0).getLanguage();
    }

    /** @return unmodifiable list of current morphological forms */
    public List<Word> getForms() {
        return Collections.unmodifiableList(forms);
    }

    /**
     * Adds an inflected form to the lexeme.
     * @param form the word form to add
     */
    public void addForm(Word form) {
        if (form != null && (forms.isEmpty() || form.getLanguage().equals(getLanguage()))) {
            forms.add(form);
        }
    }

    /** @return the lemma or base form of the lexeme */
    public String getCanonicalForm() {
        return canonicalForm;
    }

    public void setCanonicalForm(String canonicalForm) {
        this.canonicalForm = canonicalForm;
    }

    public Word.PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(Word.PartOfSpeech pos) {
        this.partOfSpeech = pos != null ? pos : Word.PartOfSpeech.UNKNOWN;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lexeme lexeme)) return false;
        return Objects.equals(canonicalForm, lexeme.canonicalForm) && 
               Objects.equals(getLanguage(), lexeme.getLanguage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(canonicalForm, getLanguage());
    }

    @Override
    public String toString() {
        return canonicalForm;
    }
}

