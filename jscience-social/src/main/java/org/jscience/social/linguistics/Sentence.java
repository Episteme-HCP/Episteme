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
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a sentence, a sequence of phrases or words that expresses 
 * a complete thought or proposition. It contains grammatical metadata 
 * about its purpose and structure.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Sentence implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Complexity categories for sentence structure.
     */
    public enum Structure {
        SIMPLE, COMPOUND, COMPLEX, COMPOUND_COMPLEX, UNKNOWN
    }

    /**
     * Communicative intent (illocutionary force) of the sentence.
     */
    public enum Purpose {
        /** Statement of fact. */
        DECLARATIVE, 
        /** Question. */
        INTERROGATIVE, 
        /** Stylistic question without expected answer. */
        RHETORICAL, 
        /** Expression of strong emotion. */
        EXCLAMATORY, 
        /** Command or request. */
        IMPERATIVE, 
        UNKNOWN
    }

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Phrase> phrases = new ArrayList<>();

    @Attribute
    private Structure structure = Structure.UNKNOWN;

    @Attribute
    private Purpose purpose = Purpose.UNKNOWN;

    @Attribute
    private String rawText;

    /**
     * Creates an empty sentence.
     */
    public Sentence() {}

    /**
     * Creates a sentence from existing phrases.
     * @param phrases list of phrases to assemble
     */
    public Sentence(List<Phrase> phrases) {
        if (phrases != null) this.phrases.addAll(phrases);
    }

    /**
     * Creates a sentence from raw textual input and attempts to detect its purpose.
     * @param text raw sentence text
     */
    public Sentence(String text) {
        this.rawText = text;
        detectPurpose(text);
    }

    private void detectPurpose(String text) {
        if (text == null || text.trim().isEmpty()) return;
        String trimmed = text.trim();
        char lastChar = trimmed.charAt(trimmed.length() - 1);
        if (lastChar == '?') this.purpose = Purpose.INTERROGATIVE;
        else if (lastChar == '!') this.purpose = Purpose.EXCLAMATORY;
        else if (lastChar == '.') {
            String lower = trimmed.toLowerCase();
            if (lower.startsWith("please ") || lower.startsWith("do ") || lower.startsWith("let ")) 
                this.purpose = Purpose.IMPERATIVE;
            else this.purpose = Purpose.DECLARATIVE;
        }
    }

    public List<Phrase> getPhrases() {
        return Collections.unmodifiableList(phrases);
    }

    public void addPhrase(Phrase phrase) {
        if (phrase != null) phrases.add(phrase);
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    /**
     * Sets the raw text of the sentence and detects its purpose.
     * @param text the raw text
     */
    public void setText(String text) {
        this.rawText = text;
        detectPurpose(text);
    }

    /**
     * Estimates word count from raw text or constituent phrases.
     * @return count of words
     */
    public int getWordCount() {
        if (rawText != null) return rawText.split("\\s+").length;
        return phrases.stream().mapToInt(Phrase::getWordCount).sum();
    }

    @Override
    public String toString() {
        if (rawText != null) return rawText;
        StringBuilder sb = new StringBuilder();
        for (Phrase p : phrases) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(p.toString());
        }
        return sb.toString();
    }
}

