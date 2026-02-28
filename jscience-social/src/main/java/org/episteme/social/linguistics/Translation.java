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

/**
 * The Translation class provides biological and linguistic translation support.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 * @since 1.0
 */
public abstract class Translation {
    /** The source text to be translated. */
    private Text text;

    /** The target language for the translation. */
    private Language target;

    /**
     * Creates a new Translation object.
     *
     * @param text   the source text
     * @param target the target language
     */
    public Translation(Text text, Language target) {
        if ((text != null) && (target != null)) {
            this.text = text;
            this.target = target;
        } else {
            throw new IllegalArgumentException(
                "The Translation constructor doesn't accept null arguments.");
        }
    }

    /**
     * Returns the source text of this translation.
     *
     * @return the source text
     */
    public Text getText() {
        return text;
    }

    /**
     * Returns the target language of this translation.
     *
     * @return the target language
     */
    public Language getTargetLanguage() {
        return target;
    }

    /**
     * Returns the translated version of the text.
     *
     * @return the translated text
     */
    public abstract Text getTranslatedText();
}

