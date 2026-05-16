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
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.episteme.core.util.Named;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a human language as a systematic linguistic entity.
 * It tracks grammatical properties, writing systems (graphemes), and 
 * phonetic inventories (phonemes).
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Language implements Named, Serializable {

    private static final long serialVersionUID = 2L;

    public static final Language ENGLISH = new Language("en", "English");

    @Id
    private final String isoCode;

    @Attribute
    private final String name;

    @Attribute
    private String nativeName;

    @Attribute
    private String scriptName;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Grapheme> graphemes = new HashSet<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Phoneme> phonemes = new HashSet<>();

    /**
     * Creates a new Language instance.
     * 
     * @param isoCode the standard ISO 639 code (e.g., "en", "fra")
     * @param name common English name of the language
     */
    public Language(String isoCode, String name) {
        this.isoCode = Objects.requireNonNull(isoCode, "ISO code cannot be null").toLowerCase();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    @Override
    public String getName() {
        return name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getCode() {
        return isoCode;
    }

    public String getId() {
        return isoCode;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    /**
     * Registers a single character in this language's grapheme inventory.
     * @param character textual character
     */
    public void addGrapheme(char character) {
        graphemes.add(new Grapheme(this, character));
    }

    /**
     * Batch adds multiple characters as graphemes.
     * @param characters string containing all characters to add
     */
    public void addGraphemes(String characters) {
        if (characters != null) {
            for (char c : characters.toCharArray()) {
                addGrapheme(c);
            }
        }
    }

    public Set<Grapheme> getGraphemes() {
        return Collections.unmodifiableSet(graphemes);
    }

    /**
     * Associates a phoneme with this language's inventory.
     * @param phoneme the phoneme to add
     */
    public void addPhoneme(Phoneme phoneme) {
        if (phoneme != null && phoneme.getLanguage().equals(this)) {
            phonemes.add(phoneme);
        }
    }

    public Set<Phoneme> getPhonemes() {
        return Collections.unmodifiableSet(phonemes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language language)) return false;
        return Objects.equals(isoCode, language.isoCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isoCode);
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", name, isoCode);
    }
}

