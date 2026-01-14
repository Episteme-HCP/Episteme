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

package org.jscience.util;

/**
 * An interface to support very simple synchronous key encryption. The entire
 * data to be encrypted needs to fit into memory, i.e. this scheme is not
 * appropriate to encode/decode large files at once.
 *
 * @author Holger Antelmann
 * @see Encoded
 */
public interface SynchronousKey {
    /**
     * decodes the given byte array
     *
     * @param encrypted DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    byte[] decode(byte[] encrypted);

    /**
     * encodes the given byte array
     *
     * @param plainSource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    byte[] encode(byte[] plainSource);

    /**
     * returns a signature that is used to identify the key that needs
     * to be known for both, encoding and decoding
     *
     * @return DOCUMENT ME!
     */
    String getKeySignature();
}
