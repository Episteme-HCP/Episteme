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

package org.jscience.ml.openmath.codec;

import org.jscience.ml.openmath.OMObject;

import java.util.Hashtable;

/**
 * A String based codec that translates (XML-encoded) OpenMath objects to
 * their backengine equivalent. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 */
public abstract class Codec {
    /**
     * Stores the CD's supported by the codec. <p>
     * <p/>
     * This hashtable is used to support CD's that are not defined in the core
     * set of CD's. Note that if you put a CD which belongs to the core in
     * this  hashtable it IS used. <p>
     */
    protected Hashtable mCDs = new Hashtable();

    /**
     * Constructor. <p>
     */
    public Codec() {
        super();
    }

    /**
     * Adds a CD to the Codec. <p>
     *
     * @param name      the name of the CD.
     * @param location  the URL of the CD, if none is given it is assume to be
     *                  on the classpath
     * @param className the Codec of the CD.
     * @throws CodecException when adding the CD (Codec) failed.
     */
    public void addCD(String name, String location, String className)
            throws CodecException {
        mCDs.put(name, location + ":" + className);
    }

    /**
     * Removes a CD from the Codec. <p>
     *
     * @param name the name of the CD.
     * @throws CodecException when removing the CD (Codec) failed.
     */
    public void removeCD(String name) throws CodecException {
        mCDs.remove(name);
    }

    /**
     * Encodes the XML-encoded OM-object. <p>
     *
     * @param object the OpenMath object in XML encoding to encode.
     * @return the encoding for the command.
     * @throws CodecEncodeException when a problem arises during encoding.
     */
    public abstract String encode(String object) throws CodecEncodeException;

    /**
     * Encodes the OM-object to backengine-syntax. <p>
     *
     * @param object the OpenMath object to encode.
     * @return the encoding for the OpenMath object.
     * @throws CodecEncodeException when a problem arises during encoding.
     */
    public abstract String encodeOMObject(OMObject object)
            throws CodecEncodeException;

    /**
     * Decodes the backengine syntax to an OpenMath object in XML encoding. <p>
     *
     * @param syntax the syntax to decode to an OpenMath object in XML encoding.
     * @return the OpenMath object in XML encoding.
     * @throws CodecDecodeException when a problem arises during decoding.
     */
    public abstract String decode(String syntax) throws CodecDecodeException;

    /**
     * Decodes the backengine-syntax to an OM-object. <p>
     *
     * @param syntax the syntax to decode to an OpenMath object.
     * @return the OpenMath object.
     * @throws CodecDecodeException when a problem arises during decoding.
     */
    public abstract OMObject decodeOMObject(String syntax)
            throws CodecDecodeException;
}
