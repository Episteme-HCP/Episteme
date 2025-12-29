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

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Sentence {

    public enum Type {
        DECLARATIVE, INTERROGATIVE, IMPERATIVE, EXCLAMATORY, UNKNOWN
    }

    private final String content;
    private final Type type;

    // In V1 legacy, it was a Vector of Phrases. Here we just store basic meta-data
    // for now.

    public Sentence(String content) {
        this(content, determineType(content));
    }

    public Sentence(String content, Type type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public Type getType() {
        return type;
    }

    private static Type determineType(String text) {
        if (text == null || text.isEmpty())
            return Type.UNKNOWN;
        String trimmed = text.trim();
        if (trimmed.endsWith("?"))
            return Type.INTERROGATIVE;
        if (trimmed.endsWith("!"))
            return Type.EXCLAMATORY;
        if (trimmed.endsWith("."))
            return Type.DECLARATIVE;
        return Type.UNKNOWN;
    }
}
