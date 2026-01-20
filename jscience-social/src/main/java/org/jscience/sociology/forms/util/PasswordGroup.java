/*
 * @(#)PasswordGroup.java  2.0, 2001/06
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
