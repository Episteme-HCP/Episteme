/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.util.identity.Identifiable;

/**
 * Represents a human language.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Language implements Identifiable<String> {

        private final String code; // ISO 639-1 (2 letters) or ISO 639-3 (3 letters)
        private final String name;
        private final String family;

        public Language(String code, String name) {
                this(code, name, null);
        }

        public Language(String code, String name, String family) {
                this.code = code;
                this.name = name;
                this.family = family;
        }

        @Override
        public String getId() {
                return code;
        }

        public String getCode() {
                return code;
        }

        public String getName() {
                return name;
        }

        public String getFamily() {
                return family;
        }

        @Override
        public String toString() {
                return String.format("%s (%s)", name, code);
        }

        // Common Languages
        public static final Language ENGLISH = new Language("en", "English", "Indo-European");
        public static final Language FRENCH = new Language("fr", "French", "Indo-European");
        public static final Language SPANISH = new Language("es", "Spanish", "Indo-European");
        public static final Language GERMAN = new Language("de", "German", "Indo-European");
        public static final Language CHINESE = new Language("zh", "Chinese", "Sino-Tibetan");
        public static final Language ARABIC = new Language("ar", "Arabic", "Afroasiatic");
}
