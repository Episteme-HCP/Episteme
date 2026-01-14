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

/* ====================================================================
 * /util/UIDGenerator.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om.util;

import java.rmi.server.UID;


/**
 * The UIDGenerator implements the IIDGenerator interface by using
 * java.rmi.server.UID, which creates pretty exact unique IDs.
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public class UIDGenerator implements IIDGenerator {
    // ------------
    // IIDGenerator ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------
    /**
     * Creates a unique ID that can be used to identify and link
     * several schema elements.<br>
     * All elements inside a XML file need to have a unique ID.
     *
     * @return Returns a unique ID that can be used to identify a schema
     *         element and to link several schema elements
     */
    public String generateUID() {
        String uid = new UID().toString();

        // XML Schema ID's should not contain ':' or '-'
        // characters, so we replace them
        uid = uid.replaceAll("-", "");
        uid = uid.replaceAll(":", "");

        // As XML Schema requires an ID not to start with a number,
        // we put a 'OM' before each ID
        return "OM" + uid;
    }
}
