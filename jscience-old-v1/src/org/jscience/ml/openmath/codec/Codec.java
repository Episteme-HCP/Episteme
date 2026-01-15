/*
 * $Id: Codec.java,v 1.2 2007-10-21 17:46:57 virtualcall Exp $
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
