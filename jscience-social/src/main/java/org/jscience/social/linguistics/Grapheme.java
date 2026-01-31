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

import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a grapheme - the minimal unit of written language.
 * <p>
 * A grapheme is the fundamental unit of a writing system, such as a letter
 * in alphabetic scripts. For example, in English, the letters a-z are graphemes.
 * Note that space (as a delimiter) is typically not considered a grapheme.
 * * @see <a href="https://en.wikipedia.org/wiki/Grapheme">Grapheme (Wikipedia)</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Grapheme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Language language;

    @Attribute
    private final char character;

    @Attribute
    private String unicodeName;

    @Attribute
    private int codePoint;

    /**
     * Creates a new Grapheme.
     *
     * @param language  the language this grapheme belongs to
     * @param character the character representation
     * @throws NullPointerException if language is null
     */
    public Grapheme(Language language, char character) {
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.character = character;
        this.codePoint = (int) character;
    }

    /**
     * Returns the language this grapheme belongs to.
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Returns the character representation.
     * @return the character
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Returns the Unicode code point.
     * @return the code point
     */
    public int getCodePoint() {
        return codePoint;
    }

    /**
     * Returns the Unicode name of this character.
     * @return the Unicode name, or null if not set
     */
    public String getUnicodeName() {
        return unicodeName;
    }

    /**
     * Sets the Unicode name.
     * @param unicodeName the Unicode character name
     */
    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    /**
     * Checks if this is a vowel (for Latin script).
     * @return true if vowel
     */
    public boolean isVowel() {
        char lower = Character.toLowerCase(character);
        return lower == 'a' || lower == 'e' || lower == 'i' || 
               lower == 'o' || lower == 'u';
    }

    /**
     * Checks if this is a consonant (for Latin script).
     * @return true if consonant
     */
    public boolean isConsonant() {
        return Character.isLetter(character) && !isVowel();
    }

    /**
     * Checks if this is an uppercase letter.
     * @return true if uppercase
     */
    public boolean isUpperCase() {
        return Character.isUpperCase(character);
    }

    /**
     * Checks if this is a lowercase letter.
     * @return true if lowercase
     */
    public boolean isLowerCase() {
        return Character.isLowerCase(character);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Grapheme)) return false;
        Grapheme other = (Grapheme) obj;
        return character == other.character && 
               Objects.equals(language, other.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language.getIsoCode(), character);
    }

    @Override
    public String toString() {
        return String.valueOf(character);
    }
}

