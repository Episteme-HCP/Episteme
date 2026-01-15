/*
 * $Id: CDCodec.java,v 1.2 2007-10-21 17:46:57 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl/
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which 
 *  case the provisions of the LGPL license are applicable instead of those 
 *  above. A copy of the LGPL license is available at http://www.gnu.org/
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
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
