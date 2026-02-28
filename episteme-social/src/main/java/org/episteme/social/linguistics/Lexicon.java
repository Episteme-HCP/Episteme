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

import org.episteme.core.util.Named;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a lexicon - a dictionary of lexemes with definitions.
 * <p>
 * A lexicon maps lexemes (abstract word units) to their definitions for
 * a specific language.
 * * @see <a href="https://en.wikipedia.org/wiki/Lexicon">Lexicon (Wikipedia)</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Lexicon implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute
    private String name;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Language language;

    @Attribute
    private final Map<String, Lexeme> entries;

    /**
     * Creates an empty lexicon.
     */
    public Lexicon() {
        this.entries = new HashMap<>();
    }

    /**
     * Creates a named lexicon.
     *
     * @param name the lexicon name
     */
    public Lexicon(String name) {
        this();
        this.name = name;
    }

    /**
     * Creates a lexicon for a specific language.
     *
     * @param name     the lexicon name
     * @param language the language
     */
    public Lexicon(String name, Language language) {
        this(name);
        this.language = language;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the lexicon name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the language.
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     * @param language the language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Adds a lexeme with definition.
     *
     * @param lexeme     the lexeme
     * @param definition the meaning
     * @throws IllegalArgumentException if language mismatch
     */
    public void put(Lexeme lexeme, String definition) {
        Objects.requireNonNull(lexeme, "Lexeme cannot be null");
        if (definition == null || definition.isEmpty()) {
            throw new IllegalArgumentException("Definition cannot be null or empty");
        }
        
        if (language == null) {
            language = lexeme.getLanguage();
        } else if (!lexeme.getLanguage().equals(language)) {
            throw new IllegalArgumentException("Lexeme language must match lexicon language");
        }
        
        lexeme.setDefinition(definition);
        entries.put(lexeme.getCanonicalForm().toLowerCase(), lexeme);
    }

    /**
     * Adds a word with definition.
     *
     * @param word       the word string
     * @param definition the meaning
     */
    public void put(String word, String definition) {
        Objects.requireNonNull(word, "Word cannot be null");
        Objects.requireNonNull(language, "Language must be set first");
        
        Lexeme lexeme = new Lexeme(word, language);
        put(lexeme, definition);
    }

    /**
     * Gets a lexeme by canonical form.
     *
     * @param word the word
     * @return the lexeme, or null if not found
     */
    public Lexeme get(String word) {
        return entries.get(word.toLowerCase());
    }

    /**
     * Gets the definition for a word.
     *
     * @param word the word
     * @return the definition, or null if not found
     */
    public String getDefinition(String word) {
        Lexeme lexeme = get(word);
        return lexeme != null ? lexeme.getDefinition() : null;
    }

    /**
     * Checks if a word is in the lexicon.
     *
     * @param word the word
     * @return true if present
     */
    public boolean contains(String word) {
        return entries.containsKey(word.toLowerCase());
    }

    /**
     * Removes a lexeme.
     *
     * @param word the word
     * @return the removed lexeme, or null
     */
    public Lexeme remove(String word) {
        Lexeme removed = entries.remove(word.toLowerCase());
        if (entries.isEmpty()) {
            language = null;
        }
        return removed;
    }

    /**
     * Returns all words in the lexicon.
     * @return set of word strings
     */
    public Set<String> getWords() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    /**
     * Returns all lexemes.
     * @return collection of lexemes
     */
    public Collection<Lexeme> getLexemes() {
        return Collections.unmodifiableCollection(entries.values());
    }

    /**
     * Returns the number of entries.
     * @return entry count
     */
    public int size() {
        return entries.size();
    }

    /**
     * Checks if the lexicon is empty.
     * @return true if empty
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Clears all entries.
     */
    public void clear() {
        entries.clear();
        language = null;
    }

    @Override
    public String toString() {
        return String.format("Lexicon[%s, %s, %d entries]", 
            name, language != null ? language.getName() : "no language", size());
    }
}

