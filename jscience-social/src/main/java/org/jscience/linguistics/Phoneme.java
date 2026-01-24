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

import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import javax.sound.sampled.Clip;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a phoneme - the minimal unit of sound in a language.
 * <p>
 * A phoneme is to spoken language what a grapheme is to written language.
 * This class uses IPA (International Phonetic Alphabet) for representation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Phoneme">Phoneme (Wikipedia)</a>
 * @see <a href="https://en.wikipedia.org/wiki/International_Phonetic_Alphabet">IPA</a>
 */
@Persistent
public class Phoneme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Language language;

    @Attribute
    private Clip clip;

    @Attribute
    private char ipaCharacter;

    @Attribute
    private String ipaSymbol;

    @Attribute
    private String description;

    /**
     * Type of phoneme.
     */
    public enum Type {
        VOWEL, CONSONANT, DIPHTHONG, AFFRICATE, OTHER
    }

    @Attribute
    private Type type;

    /**
     * Creates a phoneme with an audio clip.
     *
     * @param language the language
     * @param clip     the audio clip representing the sound
     * @throws NullPointerException if language or clip is null
     */
    public Phoneme(Language language, Clip clip) {
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.clip = Objects.requireNonNull(clip, "Clip cannot be null");
        this.type = Type.OTHER;
    }

    /**
     * Creates a phoneme with audio and IPA representation.
     *
     * @param language     the language
     * @param clip         the audio clip
     * @param ipaCharacter the IPA character representation
     */
    public Phoneme(Language language, Clip clip, char ipaCharacter) {
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.clip = clip;
        this.ipaCharacter = ipaCharacter;
        this.type = Type.OTHER;
    }

    /**
     * Creates a phoneme with IPA symbol (for multi-character IPA).
     *
     * @param language  the language
     * @param ipaSymbol the IPA symbol (can be multiple characters)
     * @param type      the phoneme type
     */
    public Phoneme(Language language, String ipaSymbol, Type type) {
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.ipaSymbol = Objects.requireNonNull(ipaSymbol, "IPA symbol cannot be null");
        this.type = type != null ? type : Type.OTHER;
        if (ipaSymbol.length() == 1) {
            this.ipaCharacter = ipaSymbol.charAt(0);
        }
    }

    /**
     * Returns the language.
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Returns the audio clip.
     * @return the clip, or null if not set
     */
    public Clip getSound() {
        return clip;
    }

    /**
     * Sets the audio clip.
     * @param clip the audio clip
     */
    public void setSound(Clip clip) {
        this.clip = clip;
    }

    /**
     * Returns the IPA character.
     * @return the IPA character
     */
    public char getCharacter() {
        return ipaCharacter;
    }

    /**
     * Returns the IPA symbol (may be multi-character).
     * @return the IPA symbol
     */
    public String getIpaSymbol() {
        return ipaSymbol != null ? ipaSymbol : String.valueOf(ipaCharacter);
    }

    /**
     * Sets the IPA symbol.
     * @param ipaSymbol the IPA representation
     */
    public void setIpaSymbol(String ipaSymbol) {
        this.ipaSymbol = ipaSymbol;
    }

    /**
     * Returns the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a description for this phoneme.
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the phoneme type.
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the phoneme type.
     * @param type the type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Checks if this is a vowel phoneme.
     * @return true if vowel
     */
    public boolean isVowel() {
        return type == Type.VOWEL;
    }

    /**
     * Checks if this is a consonant phoneme.
     * @return true if consonant
     */
    public boolean isConsonant() {
        return type == Type.CONSONANT;
    }

    /**
     * Plays the audio clip if available.
     */
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Phoneme)) return false;
        Phoneme other = (Phoneme) obj;
        return Objects.equals(getIpaSymbol(), other.getIpaSymbol()) &&
               Objects.equals(language, other.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language.getIsoCode(), getIpaSymbol());
    }

    @Override
    public String toString() {
        return String.format("/%s/", getIpaSymbol());
    }
}
