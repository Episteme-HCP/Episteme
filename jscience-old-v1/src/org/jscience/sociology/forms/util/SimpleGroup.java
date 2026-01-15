/*
 * @(#)SimpleGroup.java  2.0, 2001/06
 *
 * Copyright (C) 2001 Ilirjan Ostrovica. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
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
