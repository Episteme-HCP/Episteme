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

package org.jscience.sociology.forms.util;

import org.jscience.sociology.forms.GroupValidator;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * This class is a typical implementation of <code>GroupValidator</code>
 * interface. The criteria of validation here is : fields that belong to this
 * group are not required, but in case the user fills even one of them, then
 * they all become required.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 *
 * @see GroupValidator
 */
public class SimpleGroup implements GroupValidator {
    /**
     * DOCUMENT ME!
     */
    private String groupErrorMessage = "(this field has become required)";

    /**
     * Implements the same method of <code>GroupValidator</code>
     * interface. The default value of <code>groupErrorMessage</code> is :
     * "(this field has become required)".
     *
     * @param groupErrorMessage an error message for all fields of this group.
     */
    public void setGroupErrorMessage(String groupErrorMessage) {
        this.groupErrorMessage = groupErrorMessage;
    }

    /**
     * Implements the same method of <code>GroupValidator</code>
     * interface.
     *
     * @param nameValuePairs name-value pairs to be tested.
     *
     * @return a Hashtable of name-errormessage pairs.
     */
    public Hashtable getErrorMessages(Hashtable nameValuePairs) {
        Hashtable toReturn = new Hashtable();
        Enumeration en = nameValuePairs.keys();
        String value = null;
        String name = null;
        Object o = null;
        boolean allEmpty = true;
        boolean allFull = true;

        // looping through names
        while (en.hasMoreElements()) {
            name = (String) en.nextElement();
            o = nameValuePairs.get(name);

            if (o instanceof String[]) {
                value = ((String[]) o)[0];
            } else {
                value = (String) o;
            }

            if (value.equals("")) { // no entry
                allFull = false;
                toReturn.put(name, groupErrorMessage);
            } else { // there is entry
                allEmpty = false;
            }
        }

        if (allEmpty || allFull) {
            return null;
        } else {
            return toReturn;
        }
    }
}
