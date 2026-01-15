/*
 * @(#)TextBox.java  2.0, 2001/06
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

package org.jscience.sociology.forms;

import java.util.Vector;

/**
 * This class represents textfields and textareas, as well as serves as
 * the superclass of PasswordBox class.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see FormElement
 * @see PasswordBox
 * @see FieldValidator
 */
public class TextBox extends FormElement {

    private Vector fieldValidators;

    public TextBox() {
        setValue("");
    }

    /**
     * Adds a <code>FieldValidator</code> object to the list of validators already registered with this TextBox.
     * No adding will take place in case the coming <code>fieldValidator</code> is already registered.
     * <p/>
     * In forms.xml you add a <code>FieldValidator</code> like this:
     * <p/>
     * <p><blockquote><pre>
     * &lt;field name="field name"
     *   type="org.jscience.sociology.forms.TextBox">
     *   ...............
     *   ...............
     *   &lt;field-validator>
     *      FieldValidator object name
     *   &lt;/field-validator>
     * &lt;/field>
     * </pre></blockquote><p>
     *
     * @param fieldValidator FieldValidator object to be registered with this TextBox.
     */
    public void addFieldValidator(FieldValidator fieldValidator) {
        if (fieldValidators == null) fieldValidators = new Vector();
        if (fieldValidators.contains(fieldValidator)) return;
        fieldValidators.addElement(fieldValidator);
    }

    String getError() {
        if (isRequired() && getValue().equals("")) return getErrorMessageForRequired();
        return getErrMess();
    }

    private String getErrMess() {
        if (getValue().equals("") && !isRequired()) return "";
        if (fieldValidators != null) {
            FieldValidator fc = null;
            String err = null;
            for (int i = 0; i < fieldValidators.size(); i++) {
                fc = (FieldValidator) fieldValidators.elementAt(i);
                err = fc.getErrorMessage(getValue());
                if (!err.equals("")) return err;
            }
        }
        return "";
    }

}
