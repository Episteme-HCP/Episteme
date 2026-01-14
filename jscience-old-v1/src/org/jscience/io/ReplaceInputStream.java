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

package org.jscience.io;

import java.io.IOException;
import java.io.InputStream;


/**
 * This specialized stream replaces all occurences of a given byte pattern
 * with another throughout the stream. Also, this class can easily be
 * overwritten to e.g. only replace only the first or every second occurrence
 * or the alike.
 *
 * @author Holger Antelmann
 */
public class ReplaceInputStream extends InterceptInputStream implements InterceptInputStream.Handler {
    /** DOCUMENT ME! */
    byte[] replacePattern;

    /** DOCUMENT ME! */
    byte[] buffer;

    /** DOCUMENT ME! */
    byte[] buffer2;

    /** DOCUMENT ME! */
    int cursor;

    /** DOCUMENT ME! */
    int length;

    /** DOCUMENT ME! */
    int counter = 0;

/**
     * Creates a new ReplaceInputStream object.
     *
     * @param in             DOCUMENT ME!
     * @param searchPattern  DOCUMENT ME!
     * @param replacePattern DOCUMENT ME!
     */
    public ReplaceInputStream(InputStream in, byte[] searchPattern,
        byte[] replacePattern) {
        super(in, searchPattern);

        if (replacePattern == null) {
            throw new IllegalArgumentException(
                "replacePattern may be empty but not null");
        }

        this.replacePattern = replacePattern;
        addHandler(this);
    }

    /**
     * returns the number of replacements so far
     *
     * @return DOCUMENT ME!
     */
    public int getReplacementCount() {
        return counter;
    }

    /**
     * only used internally (implementing
     * InterceptInputStream.Handler); skips the pattern length from the stream
     * and inserts the replacementPattern
     *
     * @param stream DOCUMENT ME!
     * @param pattern DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public void patternFound(InterceptInputStream stream, byte[] pattern) {
        counter++;

        try {
            stream.skip(pattern.length);
        } catch (IOException shouldntHappenBecauseLengthIsCached) {
            throw new Error();
        }

        stream.insertBytes(replacePattern);
    }
}
