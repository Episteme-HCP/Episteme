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

package mypackage.validators;

import java.util.Hashtable;


/**
 * This class is the GroupValidator implementation for the validation in
 * group of "contact", "phone", and "contactTime" fields of Registration Form.
 */
public class ContactGroup implements org.jscience.sociology.forms.GroupValidator {
    /**
     * DOCUMENT ME!
     */
    private String groupErrorMessage;

    /**
     * DOCUMENT ME!
     *
     * @param groupErrorMessage DOCUMENT ME!
     */
    public void setGroupErrorMessage(String groupErrorMessage) {
        this.groupErrorMessage = groupErrorMessage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nameValuePairs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Hashtable getErrorMessages(Hashtable nameValuePairs) {
        String contact = (String) (nameValuePairs.get("contact"));
        String phone = (String) (nameValuePairs.get("phone"));
        String[] contactTime = (String[]) (nameValuePairs.get("contactTime"));

        Hashtable toReturn = new Hashtable();

        // if the user wants to be contacted, he must also give
        // his phone number and his prefered time.
        if (contact.equals("yes")) {
            if (phone.equals("")) {
                toReturn.put("phone", groupErrorMessage);
            }

            if (contactTime[0].equals("")) {
                toReturn.put("contactTime", groupErrorMessage);
            }
        }

        if (toReturn.size() == 0) {
            return null;
        } else {
            return toReturn;
        }
    }
}
