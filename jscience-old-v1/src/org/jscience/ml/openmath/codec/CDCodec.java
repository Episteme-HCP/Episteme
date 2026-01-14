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
import org.jscience.ml.openmath.io.OMXMLReader;
import org.jscience.ml.openmath.io.OMXMLWriter;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * A codec that translates (XML-encoded) OpenMath objects as defined in the
 * Content Dictionary it implements to their backengine equivalent and
 * vice-versa. <p>
 * <p/>
 * Note that this kind of Codec always needs a master codec as parent to
 * terminate properly. <p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.2 $
 */
public abstract class CDCodec {
    /**
     * Stores the parent Codec. <p>
     */
    protected Codec mParent = null;

    /**
     * Constructor. <p>
     */
    public CDCodec() {
        super();
    }

    /**
     * Constructor (parameterized). <p>
     *
     * @param parent the parent codec.
     */
    public CDCodec(Codec parent) {
        super();
        mParent = parent;
    }

    /**
     * Sets the parent-object. <p>
     *
     * @param parent set the parent codec.
     */
    public void setParent(Codec parent) {
        mParent = parent;
    }

    /**
     * Gets the parent-object. <p>
     *
     * @return the parent codec.
     */
    public Codec getParent() {
        return mParent;
    }

    /**
     * Encodes the XML-encoded OM-object. <p>
     *
     * @param string encodes the XML-encoded OpenMath object.
     * @return the string with the encoding.
     * @throws CodecEncodeException when a problem arises during encoding.
     */
    public String encode(String string) throws CodecEncodeException {
        try {
            StringReader stringReader = new StringReader(string);
            InputSource source = new InputSource(stringReader);
            OMXMLReader reader = new OMXMLReader(source);
            OMObject object = reader.readObject();

            return encodeOMObject(object);
        } catch (Exception exception) {
            throw new CodecEncodeException("Unable to parse OMObject: "
                    + string);
        }
    }

    /**
     * Encodes the OM-object to backengine-syntax. <p>
     *
     * @param object encodes the OpenMath object.
     * @return the string with the encoding.
     * @throws CodecEncodeException when a problem arises during encoding.
     */
    public String encodeOMObject(OMObject object) throws CodecEncodeException {
        throw new CodecEncodeException("Please implement encodeOMObject for your CDCodec: "
                + getClass().getName());
    }

    /**
     * Decodes the backengine syntax to a XML-encoded OM-object. <p>
     *
     * @param syntax decodes the syntax to an OpenMath object.
     * @return the OpenMath object in XML encoding.
     * @throws CodecDecodeException when a problem arises during decoding.
     */
    public String decode(String syntax) throws CodecDecodeException {
        StringWriter stringWriter = new StringWriter();
        OMXMLWriter xmlWriter = new OMXMLWriter(stringWriter);
        OMObject object = decodeOMObject(syntax);

        xmlWriter.writeObject(object);

        return stringWriter.toString();
    }

    /**
     * Decodes the backengine-syntax to an OM-object. <p>
     *
     * @param syntax decodes the syntax to an OpenMath object.
     * @return the OpenMath object.
     * @throws CodecDecodeException when a problem arises during decoding.
     */
    public OMObject decodeOMObject(String syntax) throws CodecDecodeException {
        throw new CodecDecodeException("Please implement decodeOMObject for your CDCodec: "
                + getClass().getName());
    }
}
