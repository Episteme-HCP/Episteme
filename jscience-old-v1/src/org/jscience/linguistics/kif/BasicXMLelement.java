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

package org.jscience.linguistics.kif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * A class designed to mirror the information in a basic XML tag.
 */
public class BasicXMLelement {
    /** The name of the tag */
    public String tagname = null;

    /** The attributes of the tag in key=value form */
    public HashMap attributes = new HashMap();

    /**
     * Any subelements of the tag, meaning any other tags that are
     * nested within this one.
     */
    public ArrayList subelements = new ArrayList();

    /** The contents between the start and end of this tag */
    public String contents = "";

    /**
     * Convert the XML element to a String.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        result = result.append("<" + tagname);

        Iterator it = attributes.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            String value = (String) attributes.get(key);
            result = result.append(" " + key + "='" + value + "'");
        }

        result = result.append(">");

        if (contents != null) {
            result = result.append(contents);
        }

        for (int i = 0; i < subelements.size(); i++) {
            BasicXMLelement el = (BasicXMLelement) subelements.get(i);
            result = result.append(el.toString());
        }

        result = result.append("</" + tagname + ">\n");

        return result.toString();
    }
}
