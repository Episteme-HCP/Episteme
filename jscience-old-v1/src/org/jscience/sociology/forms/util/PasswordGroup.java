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

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * This class is a typical implementation of <code>GroupValidator</code>
 * interface. The criteria of validation here is well known - two password
 * fields must have the same value.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 *
 * @see GroupValidator
 */
public class PasswordGroup implements org.jscience.sociology.forms.GroupValidator {
    /**
     * DOCUMENT ME!
     */
    private String groupErrorMessage = "(passwords must be the same)";

    /**
     * Implements the same method of <code>GroupValidator</code>
     * interface.
     *
     * @param groupErrorMessage an error message for both password fields of
     *        this group.
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
        Enumeration names = nameValuePairs.keys();
        Enumeration values = nameValuePairs.elements();

        if (!((String) values.nextElement()).equals(
                    (String) values.nextElement())) {
            Hashtable toReturn = new Hashtable();
            toReturn.put(names.nextElement(), groupErrorMessage);
            toReturn.put(names.nextElement(), groupErrorMessage);

            return toReturn;
        }

        return null;
    }
}
